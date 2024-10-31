package com.group20.rentify.entity;

import androidx.annotation.NonNull;

public abstract class AccountRole {
    public abstract String getRoleName();

    @NonNull
    @Override
    public String toString() {
        return getRoleName();
    }
}
