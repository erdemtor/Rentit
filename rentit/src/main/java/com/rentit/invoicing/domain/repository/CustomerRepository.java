package com.rentit.invoicing.domain.repository;

import com.rentit.invoicing.domain.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by erdem on 16.05.17.
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByEmail(String customerEmail);
}
