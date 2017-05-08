package com.rentit.common.application.exceptions;

/**
 * Created by erdem on 8.05.17.
 */
public class PurchaseOrderNotFoundException extends Exception {
    public PurchaseOrderNotFoundException(String s) {
        super("Purchase order not found with id: "+ s);
    }
}
