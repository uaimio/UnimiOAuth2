package com.oauth2.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class AuthServerConfig {

    // @Bean
    // EmbeddedDatabase datasource() {
    //     return new EmbeddedDatabaseBuilder()
    //             .setType(EmbeddedDatabaseType.H2)
    //             .setName("dashboard")
    //             .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
    //             .build();
    // }

    @Bean
    UserDetailsService users() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .passwordEncoder(encoder::encode)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // @Bean
    // JdbcUserDetailsManager users(DataSource dataSource, PasswordEncoder encoder)
    // {
    // UserDetails utente = User.builder()
    // .username("utente")
    // .password(encoder.encode("password"))
    // .roles("USER")
    // .build();
    // UserDetails admin = User.builder()
    // .username("admin")
    // .password(encoder.encode("password"))
    // .roles("ADMIN")
    // .build();
    // JdbcUserDetailsManager jdbcUserDetailsManager = new
    // JdbcUserDetailsManager(dataSource);
    // jdbcUserDetailsManager.createUser(utente);
    // jdbcUserDetailsManager.createUser(admin);
    // return jdbcUserDetailsManager;
    // }

    // @Bean
    // PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    // @Bean
    // public JWKSource<SecurityContext> jwkSource() {
    // KeyPair keyPair = generateRsaKey();
    // RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    // RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    // RSAKey rsaKey = new RSAKey.Builder(publicKey)
    // .privateKey(privateKey)
    // .keyID(UUID.randomUUID().toString())
    // .build();
    // JWKSet jwkSet = new JWKSet(rsaKey);
    // return new ImmutableJWKSet<>(jwkSet);
    // }

    // private static KeyPair generateRsaKey() {
    // KeyPair keyPair;
    // try {
    // KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    // keyPairGenerator.initialize(2048);
    // keyPair = keyPairGenerator.generateKeyPair();
    // } catch (Exception ex) {
    // throw new IllegalStateException(ex);
    // }
    // return keyPair;
    // }

}
