"""
A class responsible for parallel data creation.

Author: Dairon Pérez Frías
GitHub: https://github.com/daironpf
License: Apache License 2.0 (https://www.apache.org/licenses/LICENSE-2.0)
"""
import concurrent.futures
import os
from tqdm import tqdm

from libs.idn_range_calculator import IDNRangeCalculator

class ParallelDataCreation:

    def __init__(self):
        pass

    def generate_data_in_ranges(self, total, task_func, description):
        """
        Generate data in parallel within specified ranges.

        This method divides the data generation task into smaller ranges of identifiers (IDN) and processes them
        concurrently using multi-threading. It displays progress using tqdm.

        Args:
            total (int): Total number of elements to process.
            task_func (function): The function to execute in parallel for each range.
            description (str): Description for progress visualization.

        Returns:
            None
        """
        try:            
            # Get the list of IDNs to divide the work into smaller parts
            # id_ranges = get_idn_ranges(total)
            id_ranges = IDNRangeCalculator.calculate_ranges(total)

            # Get the maximum number of workers (threads)
            max_workers = os.cpu_count() or 1
            # Open the file where the list of paths for the files to be created is located
            with open("temp/lista.txt", "w") as file:
                # Create a pool of threads with the threadPoolExecutor
                with concurrent.futures.ThreadPoolExecutor(max_workers) as executor:                    
                    # Use tqdm to display a progress bar
                    with tqdm(total=len(id_ranges), desc=description, leave=True) as progress_bar:
                        # Send tasks to threads and run in parallel
                        futures = [executor.submit(task_func, data={"rango_idn": id_ranges[i], 'i': i, 'file': file})
                                for i in range(len(id_ranges))]
                        # Wait for all tasks to complete
                        for future in concurrent.futures.as_completed(futures):
                            try:
                                # Get the result of the task
                                _ = future.result()
                            except Exception as e:
                                # Handle any errors that may occur
                                print(f"Error: {e}")
                            finally:
                                # Update the progress bar
                                progress_bar.update(1)
                    progress_bar.refresh()
            file.close()
        except Exception as e:
            # Handle any errors that may occur outside the main loop
            print(f"Error: {e}")