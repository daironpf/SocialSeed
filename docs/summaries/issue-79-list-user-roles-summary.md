# Issue Summary: List User Roles (#79)

## Overview
Implemented a secured endpoint `GET /auth/{id}/roles` to allow users and admins to view assigned roles.

## Endpoints
- `GET /auth/{id}/roles`: Returns a list of roles (e.g., `["ROLE_USER", "ROLE_ADMIN"]`).

## Security Policy
- **Admin**: Can view roles for any `id`.
- **Self**: Can view roles if the authenticated user's ID matches the requested `id` (via username ownership check).
- **Others**: 403 Forbidden.
- **Unauthenticated**: 401 Unauthorized.

## Usage Example
```bash
curl -X GET http://localhost:8081/auth/{user-id}/roles \
  -H "Authorization: Bearer <access-token>"
```

## Response
```json
{
  "status": 200,
  "data": [
    "ROLE_USER"
  ],
  "message": "User roles retrieved successfully",
  "version": "v0.0.1",
  "timestamp": "..."
}
```
