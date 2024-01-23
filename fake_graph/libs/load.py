from libs.time_proyect import final
from controller.userController import UserController

from load.neo4j_constraint_creator import load_constraints

load_constraints()

users = UserController()

