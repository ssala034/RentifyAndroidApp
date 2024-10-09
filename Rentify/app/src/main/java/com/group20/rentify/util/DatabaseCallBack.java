package com.group20.rentify.util;

import com.group20.rentify.entity.Entity;

import java.util.ArrayList;

import kotlin.NotImplementedError;


public abstract class DatabaseCallBack{
    public void onEntityRetrieved(Entity entity) {
        throw new UnsupportedOperationException("This method must be overridden");
    }

    public void onEntityRemoval(Entity entity) {
        throw new UnsupportedOperationException("This method must be overridden");
    }

    // might try to make them non - void return types later
    public void onEntityList(ArrayList<Entity> listOfEntities){
        throw new UnsupportedOperationException("This method must be overridden");
    }

    public void onError() {
        throw new UnsupportedOperationException("This method must be overridden");
    }

}
