"""
A class responsible for loading nodes into Neo4j database from a list of files.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
import os
import concurrent.futures
import time
from tqdm import tqdm

class Neo4jLoader:

    def __init__(self):
        self.path_file = "temp/lista.txt"

    def load_nodes(self, task_func, description):
        """
        Load nodes into Neo4j database from a list of files.

        This method reads a list of file names from "temp/lista.txt" and processes each file in parallel using
        multi-threading. It displays progress using tqdm and retries any failed tasks after a delay.

        Args:
            func (function): The function to execute in parallel for each file.
            description (str): Description for progress visualization.

        Returns:
            None
        """
        try:
            # Read the list of file names from "temp/lista.txt"
            with open(self.path_file, "r") as file:
                filenames = file.read().splitlines()

                # max_workers = os.cpu_count() or 1
                # Process each file in parallel with retry
                with concurrent.futures.ThreadPoolExecutor(1) as executor:
                    with tqdm(total=len(filenames), desc=description, leave=True) as progress_bar:
                        # Submit tasks to threads and execute them in parallel for each file name
                        futures = [executor.submit(task_func, path=file_name) for file_name in filenames]

                        # Wait for all futures to complete with retry
                        for future in concurrent.futures.as_completed(futures):
                            while True:
                                try:
                                    _ = future.result()
                                    break  # Exit the retry loop if successful
                                except Exception as e:
                                    print(f"Error: {str(e)}. Retrying in 1 second...")
                                    time.sleep(1)  # Wait before retrying
                            progress_bar.update(1)

                    # Refresh the total progress
                    progress_bar.refresh()

            # Close the connection with the Neo4j database
            os.remove(self.path_file)
        except Exception as e:
            print(f"Error: {e}")

    def load_relationships(self, task_func, description):
        """
        Load nodes into Neo4j database from a list of files.

        This method reads a list of file names from "temp/lista.txt" and processes each file in parallel using
        multi-threading. It displays progress using tqdm and retries any failed tasks after a delay.

        Args:
            func (function): The function to execute in parallel for each file.
            description (str): Description for progress visualization.

        Returns:
            None
        """
        try:
            # Read the list of file names from "temp/lista.txt"
            with open(self.path_file, "r") as file:
                filenames = file.read().splitlines()
                
                # Process each file in parallel with retry
                with concurrent.futures.ThreadPoolExecutor(1) as executor:
                    with tqdm(total=len(filenames), desc=description, leave=True) as progress_bar:
                        # Submit tasks to threads and execute them in parallel for each file name
                        futures = [executor.submit(task_func, path=file_name) for file_name in filenames]

                        # Wait for all futures to complete with retry
                        for future in concurrent.futures.as_completed(futures):
                            while True:
                                try:
                                    _ = future.result()
                                    break  # Exit the retry loop if successful
                                except Exception as e:
                                    print(f"Error: {str(e)}. Retrying in 1 second...")
                                    time.sleep(1)  # Wait before retrying
                            progress_bar.update(1)

                    # Refresh the total progress
                    progress_bar.refresh()

            # Close the connection with the Neo4j database
            os.remove(self.path_file)
        except Exception as e:
            print(f"Error: {e}")