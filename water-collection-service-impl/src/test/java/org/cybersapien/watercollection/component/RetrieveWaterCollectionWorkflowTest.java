package org.cybersapien.watercollection.component;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.ignite.IgniteCache;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.processors.ProcessingState;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.util.WaterCollectionCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

/**
 * Integration test for RetrieveWaterCollectionWorkflow
 *
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DisableJmx()
public class RetrieveWaterCollectionWorkflowTest extends CamelTestSupport {
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

        return new RetrieveWaterCollectionWorkflow();
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
        WaterCollection expectedResult =
                fluentTemplate.withBody(null).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);

        assertNotNull(expectedResult);
    }
}