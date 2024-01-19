from faker import Faker
import configparser
from tqdm import tqdm
from libs.neo4j import Neo4jConnection

config = configparser.ConfigParser()
config.read('conf/generate.conf')

# neo4j 
uri = config.get('neo4j', 'uri')
user = config.get('neo4j', 'user')
pwd = config.get('neo4j', 'password')
# create the connection to neo4j
conn = Neo4jConnection(uri, user, pwd)

# The Faker config to the language in config file
faker_location = config.get('faker', 'location') 
fake = Faker([faker_location])

# %% Constraints
progress_bar = tqdm(total=6, desc="Creating Constraint")

# SocialUser
conn.query('CREATE CONSTRAINT usersId IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.id IS UNIQUE')
progress_bar.update(1)
conn.query('CREATE CONSTRAINT usersEmail IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.email IS UNIQUE')
progress_bar.update(1)
conn.query('CREATE CONSTRAINT userName IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.userName IS UNIQUE')
progress_bar.update(1)

# Post
conn.query('CREATE CONSTRAINT postId IF NOT EXISTS FOR (p:Post) REQUIRE p.id IS UNIQUE')
progress_bar.update(1)

#%% Crear Constraint temporales
conn.query('CREATE CONSTRAINT idn_user IF NOT EXISTS FOR (u:SocialUser) REQUIRE u.idn IS UNIQUE')
progress_bar.update(1)
conn.query('CREATE CONSTRAINT idn_post IF NOT EXISTS FOR (p:Post) REQUIRE p.idn IS UNIQUE')
progress_bar.update(1)
progress_bar.close()