import os
from typing import Any
import subprocess
from flask import Blueprint, render_template, Response
from flask.globals import request, session
from flask.helpers import flash, send_file
from flask_login import login_required, current_user
from . import db, upload_folder, authorize
from .models import User, CAFF, Comment

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
    if caff or file.filename.split('.')[-1] != "caff":
        return Response(response='File already uploaded', status=400)
    new_caff = CAFF(url=upload_folder+"\\"+file.filename, name=file.filename, comments =0, uploader=current_user.id)
    db.session.add(new_caff)
    db.session.commit()
    file.save(upload_folder+"\\"+file.filename)
    return Response(response="Upload succesful", status=200)

@main.route('/caffs/<int:caff_id>', methods=['GET'])
@login_required
def CAFFdetail(caff_id):
    caff = db.session.query(CAFF).get(caff_id)
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
    uploader = db.session.query(User).get(caff_id)
    subprocess.run(['Parser.exe'])
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
    comment = Comment(caff_id= caff_id, user_id=current_user.id, text=comment_text )
    db.session.add(comment)
    caff = db.session.query(CAFF).get(caff_id)
    caff.comments += 1
    db.session.commit()
    return Response(status=200)

@main.route('/caffs/<int:caff_id>/comments/<int:comment_id>', methods=['DELETE'])
@login_required
@authorize.has_role('admin')
def deleteComment(caff_id, comment_id):
    caff = db.session.query(CAFF).get(caff_id)
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
    return send_file(upload_folder+caff.name, as_attachment=True)

@main.route('/download/<randomtoken>.png')
@login_required
def downloadpreview():
    return
