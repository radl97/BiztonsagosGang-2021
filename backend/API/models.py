from . import db
from flask_login import UserMixin
from flask_authorize import RestrictionsMixin, AllowancesMixin

UserRole = db.Table(
    'user_role', db.Model.metadata,
    db.Column('user_id', db.Integer, db.ForeignKey('users.id')),
    db.Column('role_id', db.Integer, db.ForeignKey('roles.id'))
)

class User(UserMixin, db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    name = db.Column(db.String(1000))
    roles = db.relationship('Role', secondary = UserRole)

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
