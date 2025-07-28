"""
A utility class for timing processes.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
import time

class Timer:
    """
    A utility class for timing processes.
    """
    def begin(self):
        """
        Begin the timer and print a message indicating the start of the process.

        Returns:
            float: The start time in seconds since the epoch.
        """
        print("STARTING THE CREATION OF THE FAKE GRAPH")    
        return time.time()

    def end(self, start_time):
        """
        End the timer, calculate the elapsed time, and print the total execution time.

        Args:
            start_time (float): The start time of the process in seconds since the epoch.
        """
        # Get the end time
        end_time = time.time()

        # Calculate the total duration in seconds
        total_duration_seconds = end_time - start_time

        # Convert the total duration to hours, minutes, and seconds
        hours = int(total_duration_seconds // 3600)
        minutes = int((total_duration_seconds % 3600) // 60)
        seconds = int(total_duration_seconds % 60)

        # Print the total execution time
        print("THE CREATION OF THE FAKE GRAPH IS COMPLETED.")
        print(f"Total execution time: {hours:02d}h:{minutes:02d}m:{seconds:02d}s")