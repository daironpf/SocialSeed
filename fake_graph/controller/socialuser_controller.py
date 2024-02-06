"""
A controller class for managing SocialUser entities.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
from repository.socialuser_repository import SocialUserRepository
from load.configuration import config

class SocialUserController:
    """
    A controller class for managing SocialUser entities.    
    Attributes:
        __repo (SocialUserRepository): The repository for handling SocialUser data.
    """
    def __init__(self) -> None:
        """
        Initialize the SocialUserController with a SocialUserRepository instance.
        """
        self.total_user = int(config.get('users', 'TOTAL_SOCIAL_USERS'))
        self.total_post = int(config.get('posts', 'prop')) * self.total_user
        self.__repo = SocialUserRepository(self.total_user)        
            
    def load(self):
        """
        Create and load the SocialUser data.
        This method delegates the task of create and loading SocialUser nodes to the repository.
        """
        self.__repo.load_nodes()

    def friends(self):
        """
        Create and Load the friendship relationship between SocialUser nodes.
        This method delegates the task create of loading friendship relationship to the repository.
        """

        self.__repo.load_friends(
            int(config.get('users', 'friends_per_user_min')),
            int(config.get('users', 'friends_per_user_max'))
        )
    
    def followers(self):
        """
        Create and Load the followers relationship between SocialUser nodes.
        This method delegates the task create of loading followers relationship to the repository.
        """

        self.__repo.load_followers(
            int(config.get('users', 'follow_per_user_min')),
            int(config.get('users', 'follow_per_user_max'))
        )
    
    # usuario publica post
    def posted_posts(self):
        """
        Create and Load the POSTED_BY relationship between SocialUser and Post nodes.
        This method delegates the task create of loading relationship to the repository.
        """
                
        self.__repo.load_posted_post(self.total_post)
    
