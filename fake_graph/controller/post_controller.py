"""
A controller class for managing Post.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
from repository.post_repository import PostRepository
from load.configuration import config

class PostController:
    """
    A controller class for managing Post.
    Attributes:
        __repo (PostRepository): The repository for handling Post data and his HashTag.
    """

    def __init__(self) -> None:
        self.total_user = int(config.get('users', 'TOTAL_SOCIAL_USERS'))
        self.total_post = int(config.get('posts', 'prop')) * self.total_user
        self.__repo = PostRepository(self.total_post)

    def load(self):
        """
        Create and load the Post data.
        This method delegates the task of create and loading Post nodes to the repository.
        Load the config data to create in range from config file
        """

        hashtag_per_post_min = int(config.get('posts', 'hashtag_per_post_min'))
        hashtag_per_post_max = int(config.get('posts', 'hashtag_per_post_max'))
        words_per_post_min = int(config.get('posts', 'words_per_post_min'))
        words_per_post_max = int(config.get('posts', 'words_per_post_max'))

        self.__repo.load_nodes(
            hashtag_per_post_min = hashtag_per_post_min,
            hashtag_per_post_max = hashtag_per_post_max,
            words_per_post_min = words_per_post_min,
            words_per_post_max = words_per_post_max
            )