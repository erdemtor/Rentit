package com.rentit.common.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.domain.model.PlantInventoryItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */

@Service
public class MaintenanceService {
    String maintenanceURL= "http://0.0.0.0:8888/api/maintenance/plants/";

    public boolean isAvailable(List<PlantInventoryItem> items, BusinessPeriod schedule){
        return items.parallelStream().anyMatch(item-> this.isAvailable(item, schedule));
    }

    public boolean isAvailable(PlantInventoryItem item, BusinessPeriod schedule) {
        String url = maintenanceURL+item.getId()+"?startDate="+schedule.getStartDate()+"&endDate="+schedule.getEndDate();
        System.out.println(url);
        String  result;
        try {
            result = Unirest.get(url).asString().getBody();
            return result.equals("true");
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }


    }

}
