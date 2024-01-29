import configparser

class Configuration:
    def __init__(self, file_path):
        self.config = configparser.ConfigParser()
        self.read(file_path)

    def read(self, file):
        self.config.read(file)

    def get(self, section, key):
        return self.config.get(section, key)
    
    def get_uri_neo4j(self):
        location_server = config.get('neo4j','location_server')
        uri = config.get('neo4j', f'uri_{location_server}')
        return uri

config = Configuration('conf/generate.conf')