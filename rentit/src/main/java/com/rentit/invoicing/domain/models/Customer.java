package com.rentit.invoicing.domain.models;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by erdem on 16.05.17.
 */

@Entity
@Getter
public class Customer {
    @Id
    String id;
    String email;
    String base_url;
    String name;
    String password;
}
