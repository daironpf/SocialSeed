import os
import sys
from pathlib import Path

def check_dependencies(core_dir: Path):
    """Checks for illegal imports in the core directory."""
    illegal_patterns = [
        "from services",
        "import services",
        "from verify_services.e2e.services",
        "import verify_services.e2e.services"
    ]
    
    violations = []
    
    for root, _, files in os.walk(core_dir):
        for file in files:
            if file.endswith(".py") and file != "check_deps.py":
                file_path = Path(root) / file
                with open(file_path, "r") as f:
                    for i, line in enumerate(f, 1):
                        for pattern in illegal_patterns:
                            if pattern in line:
                                violations.append(f"{file_path}:{i} - Illegal import: {line.strip()}")
                                
    return violations

if __name__ == "__main__":
    current_dir = Path(__file__).parent
    violations = check_dependencies(current_dir)
    
    if violations:
        print("FAIL: Dependency check failed! Core engine must be agnostic.")
        for v in violations:
            print(v)
        sys.exit(1)
    else:
        print("SUCCESS: Core engine is agnostic (zero-coupling verified).")
        sys.exit(0)
