from load.neo4j_constraint_creator import load_constraints
from controller.socialuser_controller import SocialUserController

# create the constraints in Neo4j
load_constraints()

# mount the Controller to users in the variable users
users = SocialUserController()