package com.rentit.common.application.exceptions;

/**
 * Created by lgarcia on 2/27/2017.
 */
public class PlantNotFoundException extends Exception {
    public PlantNotFoundException(String id) {
        super("No such plantInventoryEntry with the id: "+ id);
    }
}
