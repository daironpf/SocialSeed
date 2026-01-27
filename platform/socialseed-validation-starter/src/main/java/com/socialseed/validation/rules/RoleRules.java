package com.socialseed.validation.rules;

import java.util.Set;
import java.util.HashSet;

public final class RoleRules {
    
    public static final Set<String> VALID_ROLES = Set.of(
        "ROLE_USER",
        "ROLE_ADMIN", 
        "ROLE_MODERATOR",
        "ROLE_SUPPORT"
    );
    
    private RoleRules() {
        // Utility class
    }
    
    public static boolean isValidRole(String role) {
        return role != null && VALID_ROLES.contains(role);
    }
    
    public static Set<String> getValidRoles() {
        return new HashSet<>(VALID_ROLES);
    }
}