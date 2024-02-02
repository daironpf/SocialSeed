import random
import re
from load.faker import fake

class StringGenerator:
    @staticmethod
    def generate_email(nickname: str) -> str:
        """

        @rtype: str
        """
        providers = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com']
        provider = random.choice(providers)
        email = f'{nickname}@{provider}'
        return email

    @staticmethod
    def generate_username(name: str) -> str:
        clean_name = re.sub(r'\W+', '', name.lower())
        user_name = clean_name[:6] + str(fake.random_number(digits=4))
        return user_name
