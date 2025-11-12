package cat.udl.eps.softarch.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${allowed-origins:*}")
    String[] allowedOrigins;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
            // 1. Ver negocios (GET): Permitido para todos.
            .requestMatchers(HttpMethod.GET, "/businesses", "/businesses/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/businesses").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/businesses/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/businesses/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/businesses/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/identity").authenticated()
            .requestMatchers(HttpMethod.POST, "/users").anonymous()
            .requestMatchers(HttpMethod.POST, "/users/*").denyAll()
            .requestMatchers(HttpMethod.POST, "/*").authenticated()
            .requestMatchers(HttpMethod.POST, "/*/*").authenticated()
            .requestMatchers(HttpMethod.PUT, "/*/*").authenticated()
            .requestMatchers(HttpMethod.PATCH, "/*/*").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/*/*").authenticated()
            .anyRequest().permitAll())
            .csrf((csrf) -> csrf.disable())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
            .httpBasic((httpBasic) -> httpBasic.realmName("demo"));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        String[] origins = (allowedOrigins != null && allowedOrigins.length > 0) ? allowedOrigins : new String[]{"*"};
        corsConfiguration.setAllowedOriginPatterns(Arrays.asList(origins));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
