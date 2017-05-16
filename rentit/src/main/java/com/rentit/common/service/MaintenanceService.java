package com.rentit.common.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.application.dto.MaintenanceTaskDTO;
import com.rentit.inventory.domain.model.PlantInventoryItem;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */

@Service
public class MaintenanceService {
    Type type = new TypeToken<List<MaintenanceTaskDTO>>(){}.getType();
    String maintenanceURL = "http://localhost:8888/api/maintenance/plants/";
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    public boolean isAvailable(List<PlantInventoryItem> items, BusinessPeriod schedule){
        return items.parallelStream().anyMatch(item-> this.isAvailable(item, schedule));
    }

    public boolean isAvailable(PlantInventoryItem item, BusinessPeriod schedule) {
            String url = maintenanceURL+item.getId()+"?startDate="+schedule.getStartDate()+"&endDate="+schedule.getEndDate();
            System.out.println(url);
        String  result = null;
        try {
            result = Unirest.get(url).asString().getBody();
            return result.equals("true");
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }


    }

}
