package com.group20.rentify.controller;

import com.group20.rentify.entity.Entity;

import java.util.List;

public interface Subscriber<E> {
    void notify(List<E> updatedList);
}
