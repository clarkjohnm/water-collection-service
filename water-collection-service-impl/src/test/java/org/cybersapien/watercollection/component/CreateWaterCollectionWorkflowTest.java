package org.cybersapien.watercollection.component;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.ignite.IgniteCache;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.cybersapien.watercollection.processors.ProcessingState;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Integration test for RetrieveWaterCollectionWorkflow
 *
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisableJmx()
public class CreateWaterCollectionWorkflowTest extends CamelTestSupport {
    /**
     * Apache ignite cache component
     */
    @Autowired
    private IgniteCacheComponent igniteCacheComponent;

    /**
     * WaterCollection cache
     */
    @Autowired
    private IgniteCache<String, WaterCollection> waterCollectionCache;

    @Override
    public String isMockEndpoints() {
        // override this method and return the pattern for which endpoints to mock.
        // use * to indicate all
        return "*";
    }

    @Override
    public boolean isUseAdviceWith() {
        return false;
    }

    @Override
    public RoutesBuilder createRouteBuilder() throws Exception {
        context.addComponent(ApacheCamelConfig.IGNITE_CACHE_URI_SCHEME, igniteCacheComponent);

        return new CreateWaterCollectionWorkflow.CreateWaterCollectionWorkflowBuilder()
                .newWaterCollectionPropertiesSetter(new NewWaterCollectionPropertiesSetter())
                .build();
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