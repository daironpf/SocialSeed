from repository.userRepo import UserRepo

class UserController:
    def __init__(self) -> None:
        self.__repo = UserRepo()

    #  create and load the SocialUser data.
    def cargar(self):
        self.__repo.load_nodes()