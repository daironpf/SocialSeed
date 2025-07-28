import os

class Path:
    @staticmethod
    def get_module_path(module_name: str = 'name') -> str:
        path = os.path.realpath('temp/').replace("\\", "/")
        if not os.path.exists(f'{path}/{module_name}'):
            os.makedirs(f'{path}/{module_name}')
        return f'{path}/{module_name}'