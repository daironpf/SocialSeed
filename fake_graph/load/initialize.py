# verify if Neo4j is online
from load.neo4j_status import NeoStatusChecker
checker = NeoStatusChecker()
checker.wait_for_neo4j()

# create the constraints in Neo4j
from load.neo4j_constraint_creator import load_constraints
load_constraints()

# mount the Controller to users in the variable users
from controller.socialuser_controller import SocialUserController
users = SocialUserController()

# mount the Controller to posts in the variable posts
from controller.post_controller import PostController
posts = PostController()