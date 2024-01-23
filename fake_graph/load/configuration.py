import configparser

class Configuration:
    def __init__(self, file_path):
        self.config = configparser.ConfigParser()
        self.read(file_path)

    def read(self, file):
        self.config.read(file)

    def get(self, section, key):
        return self.config.get(section, key)

config = Configuration('conf/generate.conf')