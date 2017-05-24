package com.rentit.sales.rest.controller;

import com.rentit.invoicing.domain.models.Customer;
import com.rentit.invoicing.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by erdem on 22.05.17.
 */
@RestController
@RequestMapping("/api/sales/customer")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer c){
        return customerRepository.save(Customer.createCustomer(c));
    }
}
