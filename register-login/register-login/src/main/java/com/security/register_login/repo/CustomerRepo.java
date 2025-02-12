package com.security.register_login.repo;

import com.security.register_login.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepo extends MongoRepository<Customer,String>{

    public Customer findByEmail(String email);

}
