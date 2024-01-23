from load.configuration import config
from faker import Faker

fake = Faker([config.get('faker', 'location')])