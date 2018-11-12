package org.cybersapien.watercollection.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Security configuration
 */
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Security configuration for adding users and securing API access
     */
    @Configuration
    @RequiredArgsConstructor
    @EncryptablePropertySources({@EncryptablePropertySource("classpath:credentials.yml")})
    public static class ApiSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        /**
         * The Spring Environment
         */
        private final ConfigurableEnvironment configurableEnvironment;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Add users from the properties
            int index = 0;
            boolean done = false;
            while (!done) {
                String propertyBaseName = "users[" + index + "]";
                if (configurableEnvironment.containsProperty(propertyBaseName + ".uid")) {
                    String uid = configurableEnvironment.getProperty(propertyBaseName + ".uid", String.class);
                    String password = configurableEnvironment.getProperty(propertyBaseName + ".password", String.class);
                    String roles = configurableEnvironment.getProperty(propertyBaseName + ".roles", String.class);

                    // TODO Null checks!
                    auth.inMemoryAuthentication().withUser(uid).password("{noop}"+password).roles(roles.split(","));
                    ++index;
                } else {
                    done = true;
                }
            }
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // As the name implies, authorize requests unless permission is granted to all
            http.authorizeRequests()
                    // Always allow access to the API documentation
                    .antMatchers("/swagger-ui.html*").permitAll()
                    // Authorize access to the water-collection-service API to those having the USER role
                    .antMatchers("/v1/water-collections/**").hasRole("USER")
                    // Authorize access to the management API's to those having the ADMIN role
                    .antMatchers("/actuator/**").hasRole("ADMIN")
                    .and()
                    // Disable cross site request forgery (csrf) since this is not a UI service invoked from a browser
                    .csrf().disable()
                    // Authenticate using HTTP Basic Authentication and set the WWW-Authenticate header in the response for a 401
                    .httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        }
    }
}