from libs.timer import Timer
timer = Timer()
start_time = timer.begin()

# load the configurations, mount the connections and create the constraints
from load.initialize import users

if __name__ == "__main__":
    #%% SocialUsers
    # users.load()
    users.friends()

    # users.seguidores()

    #%% Post
    # posts.cargar()
    # users.posted_posts()
    # users.like_posts()

    #%% End of generate code
    timer.end(start_time)