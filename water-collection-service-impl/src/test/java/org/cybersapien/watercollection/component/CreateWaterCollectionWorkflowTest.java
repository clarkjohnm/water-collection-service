package org.cybersapien.watercollection.component;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.ignite.IgniteCache;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.config.ApacheIgniteConfig;
import org.cybersapien.watercollection.config.ApacheIgniteDefaultConfig;
import org.cybersapien.watercollection.config.WaterCollectionServiceConfig;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.cybersapien.watercollection.processors.ProcessingState;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration test for CreateWaterCollectionWorkflow
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
public class CreateWaterCollectionWorkflowTest {
    /** WaterCollection cache */
    @Autowired
    private IgniteCache<String, WaterCollection> waterCollectionCache;

    /** FluentProducerTemplate */
    @Autowired
    private FluentProducerTemplate fluentTemplate;

    @TestConfiguration
    static class Config {
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
            return new CreateWaterCollectionWorkflow.CreateWaterCollectionWorkflowBuilder()
                    .newWaterCollectionPropertiesSetter(new NewWaterCollectionPropertiesSetter())
                    .build();
        }
    }

    /**
     * Test Create Workflow
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateWorkflow() throws Exception {
        final WaterCollection inputWaterCollection = WaterCollectionCreator.buildMinimal();

        WaterCollection outputWaterCollection =
                fluentTemplate.withBody(inputWaterCollection).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);

        assertNotNull(outputWaterCollection);
        assertNotNull(outputWaterCollection.getId());
        assertEquals(outputWaterCollection.getProcessingState(), ProcessingState.NOT_STARTED.name());
        assertNotNull(waterCollectionCache.get(outputWaterCollection.getId()));
    }

    /**
     * Test Create Workflow with null WaterCollection
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = CamelExecutionException.class)
    public void testCreateWorkflowWithNull() throws Exception {
        final String id = WaterCollectionCreator.buildMinimal().getId();

        fluentTemplate.withBody(null).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request();
    }
}