from tqdm import tqdm
from libs.neo4j import conn

class Neo4jConstraintCreator:
    
    def __init__(self, neo4j_connection):
        self.neo4j_connection = neo4j_connection        
        self.progress_bar = None

    def create_constraints(self):
        self.progress_bar = tqdm(total=6, desc="Creating Constraint")
        self.create_user_constraints()
        self.create_post_constraints()
        self.create_temporary_constraints()
        self.progress_bar.close()

    def create_user_constraints(self):
        self.neo4j_connection.query('CREATE CONSTRAINT usersId IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.id IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT usersEmail IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.email IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT userName IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.userName IS UNIQUE')
        self.progress_bar.update(1)

    def create_post_constraints(self):
        self.neo4j_connection.query('CREATE CONSTRAINT postId IF NOT EXISTS FOR (p:Post) REQUIRE p.id IS UNIQUE')
        self.progress_bar.update(1)

    def create_temporary_constraints(self):
        self.neo4j_connection.query('CREATE CONSTRAINT idn_user IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.idn IS UNIQUE')
        self.progress_bar.update(1)
        self.neo4j_connection.query('CREATE CONSTRAINT idn_post IF NOT EXISTS FOR (p:Post) REQUIRE p.idn IS UNIQUE')
        self.progress_bar.update(1)

def load_constraints():    
    # neo4j     
    constraint_creator = Neo4jConstraintCreator(conn)
    constraint_creator.create_constraints()
    conn.close()