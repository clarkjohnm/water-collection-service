package org.cybersapien.watercollection.component;

import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.config.ApacheIgniteConfig;
import org.cybersapien.watercollection.config.ApacheIgniteDefaultConfig;
import org.cybersapien.watercollection.config.WaterCollectionServiceConfig;
import org.cybersapien.watercollection.processors.ProcessingState;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheReader;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration test for RetrieveWaterCollectionsWorkflow
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
        ApacheCamelConfig.class,
        ApacheIgniteConfig.class,
        ApacheIgniteDefaultConfig.class,
        WaterCollectionServiceConfig.class
})
@EnableAutoConfiguration
@MockEndpoints
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisableJmx()
public class RetrieveWaterCollectionsWorkflowTest {
    /**
     * Ignite
     */
    @Inject
    private Ignite ignite;

    /**
     * FluentProducerTemplate
     */
    @Inject
    private FluentProducerTemplate fluentTemplate;

    @TestConfiguration
    static class Config {
        @Inject
        WaterCollectionsCacheReader waterCollectionsCacheReader;

        @Bean
        CamelContextConfiguration contextConfiguration() {
            return new CamelContextConfiguration() {
                @Override
                public void afterApplicationStart(CamelContext camelContext) {
                    // Modify camel context here
                }

                @Override
                public void beforeApplicationStart(CamelContext camelContext) {
                    // Modify camel context here
                }
            };
        }

        @Bean
        RouteBuilder routeBuilder() {
            return new RetrieveWaterCollectionsWorkflow(waterCollectionsCacheReader);
        }
    }

    /**
     * Test Retrieve Workflow
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRetrieveWorkflow() throws Exception {
        final WaterCollection inputWaterCollection = WaterCollectionCreator.buildMinimal();
        inputWaterCollection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        inputWaterCollection.setProcessingState(ProcessingState.NOT_STARTED.name());

        IgniteCache<String, WaterCollection> waterCollectionCache = ignite.getOrCreateCache(ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME);

        // Put water collection in cache
        waterCollectionCache.put(inputWaterCollection.getId(), inputWaterCollection);

        List<WaterCollection> outputWaterCollections =
                fluentTemplate.to(RetrieveWaterCollectionsWorkflow.WORKFLOW_URI).request(List.class);

        assertNotNull(outputWaterCollections);
        assertEquals(outputWaterCollections.size(), waterCollectionCache.size(CachePeekMode.ALL));
    }
}
