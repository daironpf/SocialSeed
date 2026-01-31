import importlib.util
import sys
from pathlib import Path
from typing import List, Callable, Optional


class ModuleLoader:
    """Handles discovery and dynamic loading of Python modules from file paths."""

    @staticmethod
    def load_runnable_from_file(file_path: Path, function_name: str = "run") -> Optional[Callable]:
        """Loads a specific function from a python file."""
        if not file_path.exists() or file_path.suffix != ".py":
            return None
            
        try:
            # Create a unique module name based on path to avoid collisions
            module_name = f"dynamic_mod_{file_path.stem}_{hash(str(file_path)) % 10000}"
            
            spec = importlib.util.spec_from_file_location(module_name, str(file_path))
            if spec and spec.loader:
                module = importlib.util.module_from_spec(spec)
                sys.modules[module_name] = module
                spec.loader.exec_module(module)
                
                func = getattr(module, function_name, None)
                if callable(func):
                    return func
        except Exception as e:
            print(f"Error loading {file_path}: {e}")
            
        return None

    def discover_runnables(self, root_path: Path, pattern: str = "*.py") -> List[Callable]:
        """Discovers all runnable modules in a directory matching a pattern."""
        runnables = []
        if not root_path.exists() or not root_path.is_dir():
            return runnables
            
        # Sort by filename to ensure predictable order
        paths = sorted(list(root_path.glob(pattern)), key=lambda p: p.name)
        
        for file_path in paths:
            if file_path.name == "__init__.py":
                continue
            
            runnable = self.load_runnable_from_file(file_path)
            if runnable:
                runnables.append(runnable)
                
        return runnables
