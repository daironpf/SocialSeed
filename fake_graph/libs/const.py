import configparser

config = configparser.ConfigParser()
config.read('conf/generate.conf')

# %%
# CONST

# SociaUsers
TOTAL_SOCIAL_USER = int(config.get('users', 'TOTAL_SOCIAL_USER'))