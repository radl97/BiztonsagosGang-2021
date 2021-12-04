from flask.blueprints import Blueprint
from .models import User, Role, UserRole
from . import db
from werkzeug.security import generate_password_hash
from getpass import getpass

bp = Blueprint('manager', __name__)

@bp.cli.command('init-db')
def create_db():
    db.create_all()
    db.session.commit()
    print("OK")

@bp.cli.command('admin')
def add_admin():
    # admin role
    role = Role()
    role.name = "admin"

    db.session.add(role)

    # admin user
    user = User()
    user.name = "laci"
    user.email = "laci@test.com"

    password = input("Please input a password for admin role {name} ({email})".format(name = user.name, email = user.email))
    user.password = generate_password_hash(password, method='sha256')
    db.session.add(user)
    db.session.commit()
    userRole = UserRole()
    print(user.id)
    userRole.user_id=user.id
    userRole.role_id=role.id
    db.session.add(userRole)
    db.session.commit()
