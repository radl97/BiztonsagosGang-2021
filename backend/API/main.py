import os
from typing import Any
import subprocess
from flask import Blueprint, render_template, Response, app
from flask.globals import request, session
from flask.helpers import flash, send_file, send_from_directory
from flask_login import login_required, current_user
from . import db, upload_folder, authorize, parser_wrapper
from .models import User, CAFF, Comment
import uuid

from secrets import token_hex

main = Blueprint('main', __name__)

@main.route('/caffs', methods=['GET'])
@login_required
def listCAFFs():
    #TODO good arg parse maybe good
    filter=request.args.get("filter")
    caffs = db.session.query(CAFF).all()
    json_data = []
    for caff in caffs:
        if (not filter) or (filter in caff.name):
            tmp = {"id" : caff.id,
                    "name" : caff.name,
                    "number_of_comments" : caff.comments}
            json_data.append(tmp.copy())
    return {"caffs" : json_data}

@main.route('/caffs', methods=['POST'])
@login_required
def upload():
    #prolly works
    file= request.files['file']
    caff = CAFF.query.filter_by(name=file.filename, uploader=current_user.id).first()
    if caff is not None:
        return Response(response='File already uploaded', status=400)
    if file.filename.split('.')[-1] != "caff":
        return Response(response='Not a CAFF file', status=400)

    filename = token_hex(16) + '.png'
    # Uses slash, as this is an URL
    preview_url = '/download/{filename}'.format(filename = filename)
    # Uses native separator (/ or \)
    preview_filename = os.path.join('previews', filename)

    caff_place = os.path.join(upload_folder, file.filename)
    file.save(caff_place)

    # try parse
    try:
        parser_wrapper.save_preview(caff_place, preview_filename)
    except parser_wrapper.ParsingException:
        return Response("Invalid CAFF file", status=400)

    # only access DB after parsing succeeds
    new_caff = CAFF(url=preview_url, name=file.filename, comments =0, uploader=current_user.id)
    db.session.add(new_caff)
    db.session.commit()

    return Response(response="Upload succesful", status=200)

@main.route('/caffs/<int:caff_id>', methods=['GET'])
@login_required
def CAFFdetail(caff_id):
    caff = db.session.query(CAFF).get(caff_id)
    if caff is None:
        return Response("CAFF file not found", status=404)
    comments_list = []
    comments = db.session.query(Comment).all()
    for comment in comments:
        if comment.caff_id == int(caff_id): #TODO if statment problem
            tmp = {
                "id" : comment.id,
                "text" : comment.text,
                "author" : db.session.query(User).get(comment.user_id).name
            }
            comments_list.append(tmp.copy())
    uploader = db.session.query(User).get(caff.uploader)
    json_data = {
        "id" : caff.id,
        "preview_url" : caff.url, #wtf?
        "comments" : comments_list,
        "number_of_comments" : caff.comments,
        "uploader" : {"id" : uploader.id ,"name" : uploader.name}
    }
    return json_data

@main.route('/caffs/<int:caff_id>', methods=['DELETE'])
@login_required
@authorize.has_role('admin')
def deleteCAFF(caff_id):
    #TODO admin role
    caff = db.session.query(CAFF).get(caff_id)
    if caff is None:
        return Response("CAFF file not found", status=404)
    db.session.delete(caff)
    comments = db.session.query(Comment).all()
    for comment in comments:
        if comment.caff_id == int(caff_id):
            db.session.delete(comment)
    db.session.commit()
    return Response(status=200)

@main.route('/caffs/<int:caff_id>/comments', methods=['POST'])
@login_required
def addComment(caff_id):
    comment_text = request.json["text"]
    caff = db.session.query(CAFF).get(caff_id)
    if caff is None:
        return Response("CAFF file not found", status=404)
    caff.comments += 1
    comment = Comment(caff_id = caff_id, user_id=current_user.id, text=comment_text )
    db.session.add(comment)
    db.session.commit()
    return Response(status=200)

@main.route('/caffs/<int:caff_id>/comments/<int:comment_id>', methods=['PUT'])
@login_required
@authorize.has_role('admin')
def modifyComment(caff_id, comment_id):
    new_comment_text = request.json["text"]
    caff = db.session.query(CAFF).get(caff_id)
    if caff is None:
        return Response("CAFF file not found", status=404)
    comment = db.session.query(Comment).get(comment_id)
    comment.text = new_comment_text
    db.session.commit()
    return Response(status=200)

@main.route('/caffs/<int:caff_id>/comments/<int:comment_id>', methods=['DELETE'])
@login_required
@authorize.has_role('admin')
def deleteComment(caff_id, comment_id):
    caff = db.session.query(CAFF).get(caff_id)
    if caff is None:
        return Response("CAFF file not found", status=404)
    comment = db.session.query(Comment).get(comment_id)
    db.session.delete(comment)
    caff.comments -= 1
    db.session.commit()
    return Response(status=200)

@main.route('/caffs/<int:caff_id>/download', methods=['GET'])
@login_required
def download():
    caff_id = request.args.get("id")
    caff = CAFF.query.filter_by(id=caff_id).first()
    if caff is None:
        return Response("CAFF file not found", status=404)
    return send_file(os.path.join(upload_folder, caff.name), as_attachment=True)

# Unauthorized endpoint: it could be uploaded & served in S3, or unrelated file server.
# Many production tools use authorized endpoints with unauthorized resources (with generated filenames).
@main.route('/download/<path:path>')
#@login_required
def downloadpreview(path):
    caff = CAFF.query.filter_by().first()
    if caff is None:
        return Response("CAFF file not found", status=404)
    print(path)
    return send_from_directory('../previews', path)
