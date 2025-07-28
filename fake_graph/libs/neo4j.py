"""
A utility class for establishing and managing connections to a Neo4j database.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
from neo4j import GraphDatabase
from load.configuration import config

class Neo4jConnection:
    """
    A utility class for establishing and managing connections to a Neo4j database.
    """

    def __init__(self):
        """
        Initialize the Neo4jConnection with the specified data in the config file
        """

        uri = config.get_uri_neo4j()
        user = config.get('neo4j', 'user')
        password = config.get('neo4j', 'password')
        
        try:
            self._driver = GraphDatabase.driver(uri, auth=(user, password))
        except Exception as e:
            print("Failed to create the driver:", e)

    def close(self):
        """
        Close the connection to the Neo4j database.

        This method closes the driver connection if it is not None.
        """
        if self._driver is not None:
            self._driver.close()

    def query(self, query, parameters=None):
        """
        Execute a read-write transactional query on the Neo4j database.

        Args:
            query (str): The Cypher query to execute.
            parameters (dict, optional): Parameters to pass to the query.

        Returns:
            list: The result of the query execution.
        """
        with self._driver.session() as session:
            result = session.write_transaction(self._execute_query, query, parameters)
            return result
    
    def query_insert(self, query, parameters=None):
        """
        Execute a write transactional query on the Neo4j database.

        Args:
            query (str): The Cypher query to execute.
            parameters (dict, optional): Parameters to pass to the query.

        Returns:
            list: The result of the query execution.
        """
        with self._driver.session() as session:
            result = session.run(query, **parameters) if parameters else session.run(query)
            return result


    @staticmethod
    def _execute_query(tx, query, parameters=None):
        """
        Execute the given query within the provided transaction context.

        Args:
            tx (neo4j.Transaction): The transaction object.
            query (str): The Cypher query to execute.
            parameters (dict, optional): Parameters to pass to the query.

        Returns:
            list: The result of the query execution.
        """
        result = tx.run(query, **parameters) if parameters else tx.run(query)
        return result.data()