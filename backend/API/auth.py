import re
from flask import Blueprint, render_template, redirect, url_for, request, flash, Response
from . import db, authorize
from werkzeug.security import generate_password_hash, check_password_hash
from .models import User
from flask_login import login_user, login_required, logout_user

auth = Blueprint('auth', __name__)

def pwd_stregth_check(pwd):
    if len(pwd)<8 or (not re.search("[a-z]", pwd)) or (not re.search("[A-Z]", pwd)) or not(re.search("[0-9]", pwd)) or not pwd.isascii():
        return False
    elif len(pwd)<16 and re.search("[~!@#$%^&*()_+=-]", pwd):
        return True
    elif len(pwd)>=16:
        return True
    else:
        return False

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
    return {"role" : "user"}


@auth.route('/signup', methods=['POST'])
def signup_post():
    email = request.json.get('email')
    password1 = request.json.get('password1')
    password2 = request.json.get('password2')
    name = request.json.get('name')
    user = User.query.filter_by(email=email).first()
    if not(password1==password2):
        return {
          "success" : False,
          "reason": "Passwords don't match"
        }
    if user:
        return {
          "success" : False,
          "reason": "Email address already exists"
        }
    if not pwd_stregth_check(password1):
        return {
          "success" : False,
          "reason": "Password needs to be at least 8 characters long and contain small and large captial and a number, or 16 letters long and contain a special character"
        }
    new_user = User(email=email, name=name, password=generate_password_hash(password1, method='sha256'))
    db.session.add(new_user)
    db.session.commit()
    return {
        "success" : True
    }

@auth.route('/logout')
@login_required
def logout():
    logout_user()
    return Response(status=200)
