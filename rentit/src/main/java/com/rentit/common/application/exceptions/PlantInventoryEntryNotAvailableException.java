package com.rentit.common.application.exceptions;

import java.time.LocalDate;

/**
 * Created by erdem on 13.05.17.
 */
public class PlantInventoryEntryNotAvailableException extends Exception {
    public PlantInventoryEntryNotAvailableException(String plantName, LocalDate startDate, LocalDate endDate) {
        super(plantName + " is not available in this period: "+ startDate+" : "+ endDate);
    }
}
