package com.group20.rentify.entity;

import java.util.Map;

public interface Entity {
    // this class should contain an abstract method for converting entity attributes
    // to database representation (no implementation)

    Map<String, Object> getDatabaseRepresentation();

}
