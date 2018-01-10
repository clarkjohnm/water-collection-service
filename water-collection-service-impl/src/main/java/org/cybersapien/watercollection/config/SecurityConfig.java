package org.cybersapien.watercollection.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration
 */
@Configuration
public class SecurityConfig {

    /**
     * Security configuration for adding users and securing API access
     */
    @Configuration
    @RequiredArgsConstructor
    @EncryptablePropertySource("classpath:credentials.yml")
    public static class ApiSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        /**
         * The Spring Environment
         */
        private final Environment environment;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Add users
            auth.inMemoryAuthentication().withUser("wcsuser").password(environment.getProperty("credentials.wcsuser.password")).roles("USER");
            auth.inMemoryAuthentication().withUser("wcsadmin").password(environment.getProperty("credentials.wcsadmin.password")).roles("ADMIN", "ACTUATOR");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // Secure access to the water-collection-service API
            http.httpBasic().and().authorizeRequests().antMatchers("/v1/water-collections/**").hasRole("USER");

            // Always allow access to the API documentation
            http.authorizeRequests().antMatchers("/swagger-ui.html*").permitAll();

        }
    }
}