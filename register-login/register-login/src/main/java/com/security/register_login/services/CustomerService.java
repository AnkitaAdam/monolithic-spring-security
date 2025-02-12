package com.security.register_login.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.register_login.entity.Customer;
import com.security.register_login.repo.CustomerRepo;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    public Customer saveCustomer(Customer customer){

        String encodedPwd = pwdEncoder.encode(customer.getPwd());
        customer.setPwd(encodedPwd);
        Customer savedCustomer = customerRepo.save(customer);
        return savedCustomer;

    }

    @Override
    public UserDetails loadUserByUsername(String emailid) throws UsernameNotFoundException {

        Customer customer = customerRepo.findByEmail(emailid);

        return new User(customer.getEmail(),customer.getPwd(), Collections.emptyList());

    }
}
