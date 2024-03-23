package com.oauth2.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    // return (web) -> web.ignoring().requestMatchers("/social/**");
    // }

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // return http.authorizeHttpRequests(auth -> {
    // auth.requestMatchers("/hello-world").permitAll();
    // auth.anyRequest().authenticated();
    // })
    // .oauth2Login(withDefaults())
    // .formLogin(withDefaults())
    // .build();
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.oauth2Login(Customizer.withDefaults());
    // return http.build();
    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/client-1-oidc"))
                .oauth2Client(withDefaults());
        return http.build();
    }

    // @Bean
    // WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    // ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new
    // ServletOAuth2AuthorizedClientExchangeFilterFunction(
    // authorizedClientManager);
    // return WebClient.builder()
    // .apply(oauth2Client.oauth2Configuration())
    // .build();
    // }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    // @Bean
    // WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
    // ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new
    // ServletOAuth2AuthorizedClientExchangeFilterFunction(
    // authorizedClientManager);
    // return WebClient.builder()
    // .apply(oauth2Client.oauth2Configuration())
    // .build();
    // }
}
