package com.group20.rentify.entity;

import com.google.firebase.database.DataSnapshot;
import com.group20.rentify.controller.SaveDataController;

public interface Entity {

    SaveDataController dataSaver = SaveDataController.getInstance();

    /**
     * Return the name of the entity type, usually the same as the class name
     */
    String getEntityTypeName();

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

    /**
     * @return  The display name of the entity
     */
    String getName();

    /**
     * @return  The display description for the entity
     */
    String displayDetails();

    /**
     * Destroy the entity and remove it from persistent data storage
     */
    void delete();

    /**
     * Save any changes made to the entity to persistent data storage
     */
    void save();

    void loadFurther(DataSnapshot ds);
}
