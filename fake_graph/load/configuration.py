"""
A utility class for reading and retrieving configurations from a .conf file.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
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
        location_server = self.get('neo4j','location_server')
        uri = self.get('neo4j', f'uri_{location_server}')
        return uri

config = Configuration('conf/generate.conf')