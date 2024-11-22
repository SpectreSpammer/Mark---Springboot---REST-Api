package com.onepieceofjava.SpringRestApiDemo.security.config;


import com.onepieceofjava.SpringRestApiDemo.security.repository.UserRepository;
import com.onepieceofjava.SpringRestApiDemo.security.service.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return  new CustomerUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/inventory/employees/**").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/inventory/employees/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/inventory/employees/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/inventory/employees/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/inventory/assets/**").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/inventory/assets/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/inventory/assets/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/inventory/assets/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/inventory/employees/*/assets/**").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.POST,"/api/inventory/employees/*/assets/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/inventory/employees/*/assets/**").hasAnyRole("ADMIN")
                 ).httpBasic(Customizer.withDefaults())
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();

    }


}
