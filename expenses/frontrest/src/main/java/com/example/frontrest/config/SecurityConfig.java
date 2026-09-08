package com.example.frontrest.config;


import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	            // Désactiver CSRF pour l'API REST
	            .csrf(csrf -> csrf.disable())
	            
	            // Activer CORS
	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

	            // Configuration des autorisations
	            .authorizeHttpRequests(auth -> auth
	                 //tout est public
	                .anyRequest().permitAll()
	            )
	            

	            // Configuration de la gestion des sessions (stateless pour API REST)
	            .sessionManagement(session -> session
	                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            )

	            // Désactiver les formulaires de login et basic auth
	            .formLogin(form -> form.disable())
	            .httpBasic(basic -> basic.disable());

	        return http.build();
	    }
	 
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

}