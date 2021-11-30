from flask import Blueprint, render_template, redirect, url_for, request, flash, Response
from . import db, authorize
from werkzeug.security import generate_password_hash, check_password_hash
from .models import User
from flask_login import login_user, login_required, logout_user

auth = Blueprint('auth', __name__)


@auth.route('/login', methods=['POST'])
def login_post():
    email = request.json.get('email')
    password = request.json.get('password')
    if email==None or password==None:    
        return Response(status=400)
    user = User.query.filter_by(email=email).first()
    if not user or not check_password_hash(user.password, password):
        return Response(response='Please check your login details and try again.',status=401)
    login_user(user, remember=False)
    if authorize.has_role('admin'):
        return {"role" : "admin"}
    return {"role" : ""}


@auth.route('/signup', methods=['POST'])
def signup_post():
    email = request.json.get('email')
    password1 = request.json.get('password1')
    password2 = request.json.get('password2')
    name = request.json.get('name')
    user = User.query.filter_by(email=email).first()
    if user:
        """flash('Email address already exists')"""
        return Response(response='Email address already exists', status=400)
    #password strength check
    if not(password1==password2):
        flash('Passwords don\'t match')
        return Response(response='Passwords don\'t match', status=400)
    new_user = User(email=email, name=name, password=generate_password_hash(password1, method='sha256'))
    db.session.add(new_user)
    db.session.commit()
    return Response(status=200)

@auth.route('/logout')
@login_required
def logout():
    logout_user()
    return Response(status=200)