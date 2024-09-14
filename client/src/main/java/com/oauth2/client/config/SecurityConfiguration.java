package com.oauth2.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/login",
                        "/logout",
                        "/error",
                        "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/oauth2/authorization/client1"))
                .oauth2Client(withDefaults());
        return http.build();
    }
}