package com.oauth2.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    // @Bean
    // @Order(1)
    // SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    //     OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    //     http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
    //             .oidc(withDefaults()); // Enable OpenID Connect 1.0
    //     return http.formLogin(withDefaults()).build();
    // }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                    .requestMatchers("/login")
                    .permitAll()
                    .requestMatchers("/logout")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
                .formLogin(withDefaults());
        return http.build();
    }
}