from flask.blueprints import Blueprint
from flask import Blueprint, send_from_directory
from flask_swagger_ui import get_swaggerui_blueprint

SWAGGER_URL = '/docs'
API_URL = '/swagger.yaml'

swaggerui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL,  # Swagger UI static files will be mapped to '{SWAGGER_URL}/dist/'
    API_URL,
)

bp = Blueprint('/', __name__)

bp.register_blueprint(swaggerui_blueprint)

@bp.route('/swagger.yaml')
def serve_swagger_api():
    return send_from_directory('static', 'openapi.yaml')

