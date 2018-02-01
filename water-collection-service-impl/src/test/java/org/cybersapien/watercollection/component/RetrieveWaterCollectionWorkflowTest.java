package org.cybersapien.watercollection.component;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class RetrieveWaterCollectionWorkflowTest extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        // override this method and return the pattern for which endpoints to mock.
        // use * to indicate all
        return "*";
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    public RoutesBuilder createRouteBuilder() throws Exception {
        return new RetrieveWaterCollectionWorkflow();
    }

    @Test
    public void testRoute() throws Exception {
        final int id = 99;

        Exchange exchange = fluentTemplate.withBody(id).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).send();

        assertNotNull(exchange);
    }
}