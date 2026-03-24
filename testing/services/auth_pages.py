"""Auth Service Page Object - Page Hub for Auth Service."""

from typing import Optional, Dict, Any

import requests


class APIResponse:
    """Wrapper around requests.Response for compatibility with BasePage interface."""

    def __init__(self, response: requests.Response) -> None:
        self._response = response

    @property
    def status(self) -> int:
        return self._response.status_code

    @property
    def url(self) -> str:
        return self._response.url

    def json(self) -> Dict[str, Any]:
        return self._response.json()

    def text(self) -> str:
        return self._response.text

    def headers(self) -> Dict[str, str]:
        return dict(self._response.headers)


class AuthPage:
    """Page Hub for Auth Service."""

    def __init__(self, base_url: str = "http://localhost:8085") -> None:
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({
            "Content-Type": "application/json",
            "Accept": "application/json",
        })
        self.state: Dict[str, Any] = {}

    def setup(self) -> None:
        pass

    def teardown(self) -> None:
        self.session.close()

    def check_health(self) -> bool:
        resp = self.session.get(f"{self.base_url}/actuator/health")
        return 200 <= resp.status_code < 300

    def _post(self, endpoint: str, json: Optional[Dict] = None, headers: Optional[Dict] = None) -> APIResponse:
        req_headers = dict(self.session.headers)
        if headers:
            req_headers.update(headers)
        resp = self.session.post(f"{self.base_url}{endpoint}", json=json, headers=req_headers)
        return APIResponse(resp)

    def _get(self, endpoint: str, headers: Optional[Dict] = None) -> APIResponse:
        req_headers = dict(self.session.headers)
        if headers:
            req_headers.update(headers)
        resp = self.session.get(f"{self.base_url}{endpoint}", headers=req_headers)
        return APIResponse(resp)

    def _patch(self, endpoint: str, json: Optional[Dict] = None, headers: Optional[Dict] = None) -> APIResponse:
        req_headers = dict(self.session.headers)
        if headers:
            req_headers.update(headers)
        resp = self.session.patch(f"{self.base_url}{endpoint}", json=json, headers=req_headers)
        return APIResponse(resp)

    def register(self, email: str, username: str, password: str) -> APIResponse:
        payload = {
            "email": email,
            "username": username,
            "password": password
        }
        response = self._post("/auth/register", json=payload)
        if response.status in [200, 201]:
            self.state["last_user"] = {"email": email, "username": username}
            try:
                data = response.json()
                if "data" in data:
                    self.state["access_token"] = data["data"].get("token") or data["data"].get("accessToken")
                    self.state["refresh_token"] = data["data"].get("refreshToken")
            except Exception:
                pass
        return response

    def login(self, email: str, password: str) -> APIResponse:
        payload = {
            "email": email,
            "password": password
        }
        response = self._post("/auth/login", json=payload)
        if response.status == 200:
            try:
                data = response.json()
                if "data" in data:
                    self.state["access_token"] = data["data"].get("token") or data["data"].get("accessToken")
                    self.state["refresh_token"] = data["data"].get("refreshToken")
                    self.state["user_id"] = data["data"].get("id")
            except Exception:
                pass
        return response

    def logout(self, refresh_token: Optional[str] = None) -> APIResponse:
        headers = {}
        if self.state.get("access_token"):
            headers["Authorization"] = f"Bearer {self.state['access_token']}"
        payload = {"refreshToken": refresh_token or self.state.get("refresh_token", "")}
        return self._post("/auth/logout", json=payload, headers=headers)

    def get_user_by_id(self, user_id: str) -> APIResponse:
        return self._get(f"/auth/getUserById/{user_id}")

    def get_user_by_email(self, email: str) -> APIResponse:
        return self._get(f"/auth/getUserByEmail/{email}")

    def get_user_by_username(self, username: str) -> APIResponse:
        return self._get(f"/auth/getUserByUserName/{username}")

    def change_password(self, user_id: str, current_password: str, new_password: str) -> APIResponse:
        headers = {}
        if self.state.get("access_token"):
            headers["Authorization"] = f"Bearer {self.state['access_token']}"
        payload = {
            "currentPassword": current_password,
            "newPassword": new_password
        }
        return self._post(f"/auth/{user_id}/change-password", json=payload, headers=headers)

    def change_username(self, new_username: str) -> APIResponse:
        headers = {}
        if self.state.get("access_token"):
            headers["Authorization"] = f"Bearer {self.state['access_token']}"
        payload = {"newUsername": new_username}
        return self._patch("/auth/username", json=payload, headers=headers)

    def forgot_password(self, email: str) -> APIResponse:
        payload = {"email": email}
        return self._post("/auth/forgot-password", json=payload)

    def refresh_token(self, refresh_token: str) -> APIResponse:
        payload = {"refreshToken": refresh_token}
        return self._post("/auth/token/refresh", json=payload)

    def resend_verification(self, email: str) -> APIResponse:
        payload = {"email": email}
        return self._post("/auth/resend-verification", json=payload)
