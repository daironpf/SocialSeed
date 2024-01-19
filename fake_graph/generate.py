import socket
import time

neo4j_host = "neo4j"
neo4j_port = 7474

def wait_for_neo4j():
    while True:
        try:
            # Intenta establecer una conexión con Neo4j
            with socket.create_connection((neo4j_host, neo4j_port), timeout=5):
                print("Neo4j is available!")
                break
        except (socket.error, socket.timeout) as e:
            # print(f"Waiting for Neo4j to start... Error: {e}")
            time.sleep(2)

if __name__ == "__main__":
   wait_for_neo4j()
   from libs.load import *
   #%% SocialUsers
   users.cargar()

print(f"The graph with Fake Data has been successfully created in Neo4j.")
#%%

final()