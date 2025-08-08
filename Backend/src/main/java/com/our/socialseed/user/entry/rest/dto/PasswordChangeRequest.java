package com.our.socialseed.user.entry.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeRequest {

    @NotBlank(message = "{user.currentpassword.required}")
    private String currentPassword;

    @NotBlank(message = "{user.newpassword.required}")
    private String newPassword;

    // Getters y setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
