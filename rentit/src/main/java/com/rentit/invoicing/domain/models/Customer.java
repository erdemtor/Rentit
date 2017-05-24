package com.rentit.invoicing.domain.models;

import com.rentit.inventory.infrastructure.InventoryIdentifierFactory;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by erdem on 16.05.17.
 */

@Data
@Entity
public class Customer implements GrantedAuthority{
    @Id
    String id;
    String email;
    String base_url;
    String name;
    @Column(columnDefinition="varchar(255) default 'ROLE_CUSTOMER'")
    String role = "ROLE_CUSTOMER";
    @Column(columnDefinition = "boolean default true")
    Boolean enabled = true;
    String username;
    String password;

    @Override
    public String getAuthority() {
        return getRole();
    }

    public static Customer createCustomer(Customer c){
       Customer customer = new Customer();
       customer.setId(new InventoryIdentifierFactory().nextPlantInventoryEntryID());
       customer.setBase_url(c.getBase_url());
       customer.setEmail(c.getEmail());
       customer.setName(c.getName());
       customer.setPassword(c.getPassword());

       return customer;
    }
}
