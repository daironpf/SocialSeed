"""
A utility class for creating constraints in a Neo4j database.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
from tqdm import tqdm
from libs.neo4j import Neo4jConnection

class Neo4jConstraintCreator:
    """
    A utility class for creating constraints in a Neo4j database.
    """
    
    def __init__(self, neo4j_connection):
        """
        Initialize the Neo4jConstraintCreator with the specified Neo4j connection.

        Args:
            neo4j_connection (Neo4jConnection): The connection to the Neo4j database.
        """
        self.neo4j_connection = neo4j_connection        
        self.progress_bar = None

    def create_constraints(self):
        """
        Create constraints in the Neo4j database.
        """
        self.progress_bar = tqdm(total=6, desc="Creating Constraint")
        self.create_social_user_constraints()
        self.create_post_constraints()
        self.create_temporary_constraints()
        self.progress_bar.close()

    def create_social_user_constraints(self):
        """
        Create constraints for SocialUser nodes in the Neo4j database.
        """
        self.neo4j_connection.query('CREATE CONSTRAINT usersId IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.id IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT usersEmail IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.email IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT userName IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.userName IS UNIQUE')
        self.progress_bar.update(1)

    def create_post_constraints(self):
        """
        Create constraints for Post nodes in the Neo4j database.
        """
        self.neo4j_connection.query('CREATE CONSTRAINT postId IF NOT EXISTS FOR (p:Post) REQUIRE p.id IS UNIQUE')
        self.progress_bar.update(1)

    def create_temporary_constraints(self):
        """
        Create temporary constraints for integer ranges in the Neo4j database.
        """
        self.neo4j_connection.query('CREATE CONSTRAINT idn_user IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.idn IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT idn_post IF NOT EXISTS FOR (p:Post) REQUIRE p.idn IS UNIQUE')
        self.progress_bar.update(1)

def load_constraints():
    """
    Load constraints into the Neo4j database.
    """    
    conn = Neo4jConnection()    
    constraint_creator = Neo4jConstraintCreator(conn)
    constraint_creator.create_constraints()
    conn.close()