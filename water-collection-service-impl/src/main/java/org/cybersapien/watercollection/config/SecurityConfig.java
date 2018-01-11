package org.cybersapien.watercollection.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
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
    @Slf4j
    public static class ApiSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        /**
         * The Spring Environment
         */
        private final ConfigurableEnvironment configurableEnvironment;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Dynamically add users by iterating over the properties and finding the credentials property that ends with "roles"
            MutablePropertySources mutablePropertySources = configurableEnvironment.getPropertySources();
            for (PropertySource<?> propertySource : mutablePropertySources) {
                if (propertySource instanceof EnumerablePropertySource) {
                    EnumerablePropertySource enumerablePropertySource = (EnumerablePropertySource) propertySource;
                    String[] propertyNames = enumerablePropertySource.getPropertyNames();
                    for (int i = 0; i < propertyNames.length; ++i) {
                        if (propertyNames[i].startsWith("credentials") && propertyNames[i].endsWith("roles")) {
                            String[] nameComponents = propertyNames[i].split("\\.");
                            if (nameComponents.length == 3) {
                                String userId = nameComponents[1];
                                String password = configurableEnvironment.getProperty("credentials." + userId + ".password");
                                String roles = configurableEnvironment.getProperty("credentials." + userId + ".roles");

                                if ((userId != null) && (password != null) && (roles != null)) {
                                    auth.inMemoryAuthentication().withUser(userId).password(password).roles(roles.split(","));
                                } else {
                                    log.error("Credentials cannot be added for " + userId);
                                }
                            }
                        }
                    }
                }
            }
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