"""
Script to execute the project, including loading configurations, setting up connections,
and creating constraints in Neo4j.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
import sys
sys.stdout.reconfigure(encoding='utf-8')
# Import Timer class from libs
from libs.timer import Timer

# Instantiate Timer class
timer = Timer()

# Start the timer
start_time = timer.begin()

# Load configurations, set up connections, and create constraints
from load.initialize import users, posts

if __name__ == "__main__":
    # Create and load SocialUsers
    users.load()
    # # Create and load Post data
    posts.load()

    # # Create and load data for relationships
    users.friends()
    users.followers()
    users.posted_posts()
    users.liked_posts()
    users.interested_in_hashtag()

    # End of generate code
    timer.end(start_time)