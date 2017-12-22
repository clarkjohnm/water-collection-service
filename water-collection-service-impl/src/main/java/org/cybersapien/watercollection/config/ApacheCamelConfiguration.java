package org.cybersapien.watercollection.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.component.ignite.idgen.IgniteIdGenComponent;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Configuration for Apache Camel
 */
@Configuration
public class ApacheCamelConfiguration {

    /**
     * Scheme for accessing ignite id generation in a URI
     */
    public static final String IGNITE_IDGEN_URI_SCHEME = "ignite-idgen";

    /**
     * Scheme for accessing ignite cache in a URI
     */
    public static final String IGNITE_CACHE_URI_SCHEME = "ignite-cache";

    /**
     * Apache Ignite instance
     */
    @Autowired
    private Ignite ignite;

    /**
     * Apache Camel instance
     */
    @Autowired
    private CamelContext camelContext;

    /**
     * Register Ignite id generator component
     */
    @PostConstruct
    protected void registerIgniteIdGenComponent() {
        camelContext.addComponent(IGNITE_IDGEN_URI_SCHEME, IgniteIdGenComponent.fromIgnite(ignite));
    }

    /**
     * Register Ignite cache component
     */
    @PostConstruct
    protected void registerIgniteCacheComponent() {
        camelContext.addComponent(IGNITE_CACHE_URI_SCHEME, IgniteCacheComponent.fromIgnite(ignite));
    }
}
