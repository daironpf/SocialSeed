"""Quick test script to verify auth service connectivity."""

from socialseed_e2e.core.base_page import BasePage

def test_gateway():
    """Test connection to API Gateway."""
    page = BasePage("http://localhost:8084")
    page.setup()
    try:
        r = page.get("/actuator/health")
        print(f"Gateway Status: {r.status}")
        print(f"Body: {r.json()}")
        return True
    except Exception as e:
        print(f"Error: {e}")
        return False
    finally:
        page.teardown()

if __name__ == "__main__":
    test_gateway()
