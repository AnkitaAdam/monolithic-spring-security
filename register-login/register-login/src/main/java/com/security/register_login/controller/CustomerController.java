package com.security.register_login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.register_login.entity.Customer;
import com.security.register_login.services.CustomerService;
import com.security.register_login.services.JwtService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?>registerCustomer(@RequestBody Customer customer){
        Customer savedCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody Customer customer){

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(customer.getEmail(),
                        customer.getPwd());


        Authentication authentication = authenticationManager.authenticate(token);

        boolean status = authentication.isAuthenticated();

        if(status){
//            return new ResponseEntity<>("Login Successful & WELCOME TO THE WORLD OF " +
//                    "SPRING SECURITY", HttpStatus.OK);
            String jwtToken = jwtService.generateToken(customer.getEmail());
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);

        }else{
            return new ResponseEntity<>("Login Unsuccessful....REGRET",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "WELCOME TO THE WORLD OF SPRING SECURITY";
    }
}
