from . import db
from flask_login import UserMixin
from flask_authorize import AllowancesMixin

class User(UserMixin, db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    name = db.Column(db.String(1000))
    roles = db.relationship('Role', secondary = 'user_role')

class CAFF(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    url = db.Column(db.String(10000))
    name = db.Column(db.String(1000))
    comments = db.Column(db.Integer)
    uploader = db.Column(db.Integer, db.ForeignKey(User.id))
    

class Comment(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    caff_id = db.Column(db.Integer, db.ForeignKey(CAFF.id))
    user_id = db.Column(db.Integer, db.ForeignKey(User.id))
    text = db.Column(db.String(10000))

class Role(db.Model, AllowancesMixin):
    __tablename__ = 'roles'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False, unique=True)

class UserRole(db.Model):
    __tablename__ = 'user_role'
    user_id = db.Column(db.Integer, db.ForeignKey(User.id), primary_key=True)
    role_id = db.Column(db.Integer, db.ForeignKey(Role.id), primary_key=True)
