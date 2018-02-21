package org.cybersapien.watercollection.component;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.ignite.cache.IgniteCacheComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.config.ApacheIgniteConfig;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx()
public class CreateWaterCollectionWorkflowTest extends CamelTestSupport {
    /**
     * Apache ignite instance
     */
    @Autowired
    Ignite ignite;

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
        context.addComponent(ApacheCamelConfig.IGNITE_CACHE_URI_SCHEME, IgniteCacheComponent.fromIgnite(ignite));

        return new CreateWaterCollectionWorkflow();
    }

    @Test
    public void testCreateWorkflow() throws Exception {
        final WaterCollection waterCollection = WaterCollectionCreator.buildMinimal();
        final String id = waterCollection.getId();

        IgniteCache<String, WaterCollection> waterCollectionCache =
                ignite.getOrCreateCache(ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME);

        fluentTemplate.withBody(waterCollection).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request();

        assertNotNull(waterCollectionCache.get(id));
    }

    @Test(expected = CamelExecutionException.class)
    public void testCreateWorkflowWithNull() throws Exception {
        final String id = WaterCollectionCreator.buildMinimal().getId();

        IgniteCache<String, WaterCollection> waterCollectionCache =
                ignite.getOrCreateCache(ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME);

        fluentTemplate.withBody(null).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request();
    }
}