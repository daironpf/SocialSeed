import string
import random

class UniqueCodeGenerator:
    @staticmethod
    def generate_str(length: int) -> str:
        characters = string.ascii_letters + string.digits
        code = ''.join(random.choice(characters) for _ in range(length))
        return code

    @staticmethod
    def generate_long(length: int) -> str:
        characters = string.digits
        code = ''.join(random.choice(characters) for _ in range(length))
        return code