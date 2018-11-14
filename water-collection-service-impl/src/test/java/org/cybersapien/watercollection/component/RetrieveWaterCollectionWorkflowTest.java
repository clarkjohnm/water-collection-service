package org.cybersapien.watercollection.component;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpoints;
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

import javax.cache.Cache;
import javax.inject.Inject;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Integration test for RetrieveWaterCollectionWorkflow
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
public class RetrieveWaterCollectionWorkflowTest {
    /**
     * Water collection cache
     */
    @Inject
    private Cache<String, WaterCollection> waterCollectionCache;

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
            return new RetrieveWaterCollectionWorkflow(waterCollectionsCacheReader);
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

        // Put water collection in cache
        waterCollectionCache.put(inputWaterCollection.getId(), inputWaterCollection);

        WaterCollection outputWaterCollection =
                fluentTemplate.withBody(inputWaterCollection.getId()).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);

        assertNotNull(outputWaterCollection);
        assertEquals(inputWaterCollection.getId(), outputWaterCollection.getId());
        assertNotNull(waterCollectionCache.get(outputWaterCollection.getId()));
    }

    /**
     * Test Retrieve Workflow with null WaterCollection id
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = CamelExecutionException.class)
    public void testRetrieveWorkflowWithNullId() throws Exception {
        fluentTemplate.withBody(null).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);
    }

    /**
     * Test Retrieve Workflow with unknown WaterCollection id
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRetrieveWorkflowWithUnknownId() throws Exception {
        WaterCollection expectedResult =
                fluentTemplate.withBody("id of nonexistent").to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);

        assertNull(expectedResult);
    }
}