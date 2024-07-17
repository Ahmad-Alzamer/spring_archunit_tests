package com.aalzamer.ArchunitTest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        var x = new UrlBasedCorsConfigurationSource();
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        x.registerCorsConfiguration("/**", corsConfiguration);
        return x;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //https://github.com/spring-projects/spring-security/issues/12546
                //https://stackoverflow.com/a/74719519/8735058
                //https://jessitron.com/2020/06/15/spring-security-for-h2-console/
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().anonymous()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    UserDetailsManager inMemoryUserDetailsManager() {

        return new InMemoryUserDetailsManager(
                User.withUsername("user")
                        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
                .password("test")
                .roles("USER").build()
        );
    }
}
