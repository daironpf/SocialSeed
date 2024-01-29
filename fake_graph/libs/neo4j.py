from neo4j import GraphDatabase
from load.configuration import config

class Neo4jConnection:
    def __init__(self, uri, user, password):
        try:
            self._driver = GraphDatabase.driver(uri, auth=(user, password))
        except Exception as e:
            print("Failed to create the driver:", e)

    def close(self):
        if self._driver is not None:
            self._driver.close()

    def query(self, query, parameters=None):
        with self._driver.session() as session:
            result = session.write_transaction(self._execute_query, query, parameters)
            return result
    
    def query_insert(self, query, parameters=None):
        with self._driver.session() as session:
            result = session.run(query, **parameters) if parameters else session.run(query)
            return result


    @staticmethod
    def _execute_query(tx, query, parameters=None):
        result = tx.run(query, **parameters) if parameters else tx.run(query)
        return result.data()

conn = Neo4jConnection(
    config.get_uri_neo4j(),
    config.get('neo4j', 'user'),
    config.get('neo4j', 'password')
)