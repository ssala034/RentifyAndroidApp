package com.group20.rentify.entity;

public abstract class UserRole extends AccountRole {
    private boolean enabled;

    public void setEnabled(boolean isEnabled) {
        enabled = isEnabled;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
