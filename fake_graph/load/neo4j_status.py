"""
A utility class for checking the status of a Neo4j database server.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
import socket
import time
from urllib.parse import urlparse
from load.configuration import config

class NeoStatusChecker:
    """
    A utility class for checking the status of a Neo4j database server.
    """
    def __init__(self):
        """
        Initialize the NeoStatusChecker with the host and port parsed from the Neo4j URI.
        """
        parsed_uri = urlparse(config.get_uri_neo4j())
        self.host = parsed_uri.hostname
        self.port = parsed_uri.port
                
    def wait_for_neo4j(self):
        """
        Continuously attempt to establish a connection with the Neo4j database server until successful.

        This method attempts to establish a connection with the specified host and port,
        and waits for the Neo4j database server to become available.
        """
        while True:
            try:
                with socket.create_connection((self.host, self.port), timeout=5):
                    print("Neo4j is available!")
                    break
            except (socket.error, socket.timeout) as e:
                print(f"Waiting for Neo4j to start... Error: {e}")
                time.sleep(5)