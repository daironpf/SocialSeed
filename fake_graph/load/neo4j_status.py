import socket, time
from urllib.parse import urlparse

from load.configuration import config

class NeoStatusChecker:
    def __init__(self):
        parsed_uri = urlparse(config.get_uri_neo4j())
        self.host = parsed_uri.hostname
        self.port = parsed_uri.port
                
    def wait_for_neo4j(self):        
        while True:
            try:
                with socket.create_connection((self.host, self.port), timeout=5):
                    print("Neo4j is available!")
                    break
            except (socket.error, socket.timeout) as e:
                print(f"Waiting for Neo4j to start... Error: {e}")
                time.sleep(5)