package com.rentit.invoicing.domain.repository;

import com.rentit.invoicing.domain.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by erdem on 16.05.17.
 */
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByEmail(String customerEmail);
}
