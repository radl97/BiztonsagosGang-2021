from . import db
from flask_login import UserMixin

class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    name = db.Column(db.String(1000))

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
