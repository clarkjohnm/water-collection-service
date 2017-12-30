package org.cybersapien.watercollection.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.DefaultFluentProducerTemplate;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.component.ignite.cache.IgniteCacheEndpoint;
import org.apache.camel.component.ignite.idgen.IgniteIdGenComponent;
import org.apache.camel.component.ignite.idgen.IgniteIdGenEndpoint;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for Apache Camel
 */
@RequiredArgsConstructor
@Configuration
public class ApacheCamelConfiguration {

    /**
     * Scheme for accessing ignite id generation in a URI
     */
    private static final String IGNITE_IDGEN_URI_SCHEME = "ignite-idgen";

    /**
     * Scheme for accessing ignite cache in a URI
     */
    private static final String IGNITE_CACHE_URI_SCHEME = "ignite-cache";

    /**
     * Apache Ignite instance
     */
    private final Ignite ignite;

    /**
     * Apache Camel context
     */
    private final CamelContext camelContext;

    /**
     * Ignite cache endpoint
     *
     * @return the ignite cache endpoint
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public IgniteCacheEndpoint igniteCacheEndpoint() {
        IgniteCacheEndpoint igniteCacheEndpoint;

        final String igniteCacheURI = ApacheCamelConfiguration.IGNITE_CACHE_URI_SCHEME + ":"
                + ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_CACHE_NAME + "?";

        igniteCacheEndpoint = new IgniteCacheEndpoint(igniteCacheURI,
                ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_CACHE_NAME, null, IgniteCacheComponent.fromIgnite(ignite));

        return igniteCacheEndpoint;
    }

    /**
     * Ignite idgen endpoint
     *
     * @return the ignite idgen endpoint
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public IgniteIdGenEndpoint igniteIdGenEndpoint() throws Exception {
        IgniteIdGenEndpoint igniteIdGenEndpoint;

        final String igniteIdGenURI = ApacheCamelConfiguration.IGNITE_IDGEN_URI_SCHEME + ":"
                + ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_SEQUENCE_NAME + "?";

        igniteIdGenEndpoint = new IgniteIdGenEndpoint(igniteIdGenURI,
                ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_SEQUENCE_NAME, null, IgniteIdGenComponent.fromIgnite(ignite));

        return igniteIdGenEndpoint;
    }

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
}
