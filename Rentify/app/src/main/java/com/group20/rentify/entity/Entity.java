package com.group20.rentify.entity;

public interface Entity {

    /**
     * Return the identifier which uniquely identifies the entity
     */
    String getUniqueIdentifier();

    /**
     * Set the identifier which uniquely identifies the entity
     * @param id    The new identifier
     * @return      True if the new identifier is set successfully, false otherwise
     */
    boolean setUniqueIdentifier(String id);
}
