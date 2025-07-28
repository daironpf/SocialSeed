"""
Initialization module for creating class instances and setting up Neo4j constraints.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
# Import the NeoStatusChecker class from the neo4j_status module
from load.neo4j_status import NeoStatusChecker

# Create an instance of NeoStatusChecker to verify if Neo4j is online
checker = NeoStatusChecker()

# Wait for Neo4j to become online
checker.wait_for_neo4j()

# Import the load_constraints function from the neo4j_constraint_creator module
from load.neo4j_constraint_creator import load_constraints

# Create the constraints in Neo4j
load_constraints()

# Import the SocialUserController class from the socialuser_controller module
from controller.socialuser_controller import SocialUserController

# Instantiate the SocialUserController and assign it to the variable users
users = SocialUserController()

# Import the PostController class from the post_controller module
from controller.post_controller import PostController

# Instantiate the PostController and assign it to the variable posts
posts = PostController()
