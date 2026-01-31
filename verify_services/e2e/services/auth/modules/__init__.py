# E2E Testing Framework - Auth Service Modules
# =============================================
# 
# This package contains test modules for the Auth service.
# Each module should export a `run(context)` function.
# 
# Module naming convention: {NN}_{description}_flow.py
# where NN is a zero-padded number for execution ordering.

from . import (
    _01_register_flow as register_flow,
    _02_login_flow as login_flow,
    _03_change_email_flow as change_email_flow,
    _04_change_password_flow as change_password_flow,
    _05_change_username_flow as change_username_flow,
    _06_refresh_flow as refresh_flow,
    _10_forgot_password_flow as forgot_password_flow,
    _11_reset_password_flow as reset_password_flow,
    _12_verify_email_flow as verify_email_flow,
    _13_resend_verification_flow as resend_verification_flow,
    _20_credential_expiration_flow as credential_expiration_flow,
    _99_logout_flow as logout_flow,
)

__all__ = [
    'register_flow',
    'login_flow',
    'change_email_flow',
    'change_password_flow',
    'change_username_flow',
    'refresh_flow',
    'forgot_password_flow',
    'reset_password_flow',
    'verify_email_flow',
    'resend_verification_flow',
    'credential_expiration_flow',
    'logout_flow',
]
