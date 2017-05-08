package com.rentit.sales.web.controller;

import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.application.service.InventoryService;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.application.service.SalesService;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    SalesService salesService;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", salesService.findAll());
        return "dashboard/orders/orders";
    }


    @PostMapping("/catalog/query")
    public String queryPlantCatalog(Model model, CatalogQueryDTO query) {
        System.out.println(query.toString());
        List<PlantInventoryEntryDTO> plants = inventoryService.findAvailablePlants(
                query.getName(),
                query.getRentalPeriod().getStartDate(),
                query.getRentalPeriod().getEndDate()
        );
        model.addAttribute("plants", plants);
        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setRentalPeriod(query.getRentalPeriod());
        model.addAttribute("po", po);
        return "dashboard/catalog/query-result";
    }

    @PostMapping("/orders")
    public String createPurchaseOrder(Model model, PurchaseOrderDTO purchaseOrderDTO) throws PlantNotFoundException {
        purchaseOrderDTO = salesService.createPurchaseOrder(purchaseOrderDTO);
        model.addAttribute("po", purchaseOrderDTO);
        return "redirect:/dashboard/orders/" + purchaseOrderDTO.get_id();
    }

    @GetMapping("/orders/{id}/updatestatus")
    public String updateStatus(Model model, @PathVariable String id, @RequestParam POStatus status) throws PurchaseOrderNotFoundException {
        salesService.updateStatus(id, status);
        return "redirect:/dashboard/orders/"+id;
    }

    @GetMapping("/orders/{id}")
    public String showPurchaseOrder(Model model, @PathVariable String id) throws PurchaseOrderNotFoundException {
        PurchaseOrderDTO po = salesService.findPurchaseOrder(id);
        model.addAttribute("po", po);
        return "dashboard/orders/show";
    }

    @ExceptionHandler({PurchaseOrderNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String bindExceptionHandler(Exception ex){
       return "404";
    }
}
