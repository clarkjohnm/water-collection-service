package org.cybersapien.watercollection.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.DefaultFluentProducerTemplate;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.component.ignite.idgen.IgniteIdGenComponent;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Configuration for Apache Camel
 */
@RequiredArgsConstructor
@Configuration
public class ApacheCamelConfig {

    /**
     * Scheme for accessing ignite id generation in a URI
     */
    private static final String IGNITE_IDGEN_URI_SCHEME = "ignite-idgen";

    /**
     * Scheme for accessing ignite cache in a URI
     */
    private static final String IGNITE_CACHE_URI_SCHEME = "ignite-cache";

    /**
     * URI for accessing ignite cache containing water collections
     */
    public static final String WATER_COLLECTION_CACHE_URI =
            ApacheCamelConfig.IGNITE_CACHE_URI_SCHEME + ":" + ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME;

    /**
     * URI for accessing ignite id generator for water collections
     */
    public static final String WATER_COLLECTION_IDGEN_URI =
            ApacheCamelConfig.IGNITE_IDGEN_URI_SCHEME + ":" + ApacheIgniteConfig.IGNITE_WATER_COLLECTION_SEQUENCE_NAME;

    /**
     * Apache Ignite instance
     */
    private final Ignite ignite;

    /**
     * Apache Camel context
     */
    private final CamelContext camelContext;

    /**
     * Apache Camel FluentProducerTemplate
     *
     * @return the FluentProducerTemplate
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public FluentProducerTemplate fluentProducerTemplate() {
        return new DefaultFluentProducerTemplate(camelContext);
    }

    /**
     * Initialize the camel context with ignite components
     *
     * @throws Exception if an exception occurs initializing the camel context
     */
    @PostConstruct
    void initializeContext() throws Exception {
        camelContext.addComponent(IGNITE_CACHE_URI_SCHEME, IgniteCacheComponent.fromIgnite(ignite));
        camelContext.addComponent(IGNITE_IDGEN_URI_SCHEME, IgniteIdGenComponent.fromIgnite(ignite));
    }
}
