package com.security.register_login.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.register_login.filter.AppFilter;
import com.security.register_login.services.CustomerService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private AppFilter filter;

    //it is used to load the users from database by calling the loadUserByName()
    // provided by spring security and gives those details to authentication manager
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(pwdEncoder());
        authenticationProvider.setUserDetailsService(customerService);

        return authenticationProvider;
    }

    //it is used to check user credentials are valid or not... authenticate the user
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();


    }
    @Bean
    public BCryptPasswordEncoder pwdEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity
//                .csrf(csrf -> csrf.disable())  // ✅ Correct way in Spring Security 6
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers("/customers/register", "/customers/login").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(withDefaults()); // Enables default login page
//
//        return httpSecurity.build();

        return httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/customers/login","/customers/register").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/customers/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();

    }

}
