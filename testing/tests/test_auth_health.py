"""Test module for Auth Service Health Check."""

from services.auth_pages import AuthPage


class TestAuthHealth:
    """Health check tests for Auth Service."""

    def test_auth_service_health(self, auth_page: AuthPage) -> None:
        """Test that auth service is healthy."""
        assert auth_page.check_health() is True

    def test_auth_service_health_endpoint(self, auth_page: AuthPage) -> None:
        """Test health endpoint returns UP status."""
        response = auth_page._get("/actuator/health")
        assert response.status == 200
        data = response.json()
        assert data.get("status") == "UP"
