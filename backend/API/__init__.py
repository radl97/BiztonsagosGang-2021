import os
from flask import Flask
from werkzeug.datastructures import Authorization
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager
from flask_authorize import Authorize

# init SQLAlchemy so we can use it later in our models
db = SQLAlchemy()
authorize = Authorize()
upload_folder ="uploads\\"

def create_app():
    app = Flask(__name__)

    if not os.path.exists(upload_folder):
        os.mkdir(upload_folder)

    app.config['SECRET_KEY'] = 'secret-key-goes-here'
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite'
    app.config['UPLOAD_FOLDER'] = upload_folder
    app.config['CORS_HEADERS'] = 'Content-Type'
    #max size?

    db.init_app(app)
    login_manager = LoginManager()
    #login_manager.login_view = 'auth.login'
    login_manager.init_app(app)
    authorize.init_app(app)

    from .models import User

    @login_manager.user_loader
    def load_user(user_id):
        return User.query.get(int(user_id))

    from .auth import auth as auth_blueprint
    app.register_blueprint(auth_blueprint)

    from .main import main as main_blueprint
    app.register_blueprint(main_blueprint)

    from .db_manager import bp as db_manager_blueprint
    app.register_blueprint(db_manager_blueprint)

    # for testing with Swagger
    if app.debug:
        from .swagger import bp as swagger_blueprint
        app.register_blueprint(swagger_blueprint)

    return app