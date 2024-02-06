from repository.post_repository import PostRepository
from load.configuration import config

class PostController:

    def __init__(self, total_post) -> None:
        self.__repo = PostRepository(total_post)

    def load(self):
        """
        Create and load the Post data.
        This method delegates the task of create and loading Post nodes to the repository.
        """
        self.__repo.load_nodes()