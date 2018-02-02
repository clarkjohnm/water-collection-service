package org.cybersapien.watercollection.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ignite.IgniteConstants;
import org.apache.camel.component.ignite.cache.IgniteCacheOperation;
import org.apache.camel.model.RouteDefinition;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.springframework.stereotype.Component;

/**
 * Workflow for creating water collections
 */
@Slf4j
@Component
public class CreateWaterCollectionWorkflow extends RouteBuilder {
    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + CreateWaterCollectionWorkflow.class.getName();

    @Override
    public void configure() throws Exception {
        RouteDefinition worflowDefinition = from(WORKFLOW_URI);
        worflowDefinition.routeId(CreateWaterCollectionWorkflow.class.getSimpleName());
        worflowDefinition.setExchangePattern(ExchangePattern.InOut);

        worflowDefinition
                .log("Message received on " + WORKFLOW_URI)

                .setHeader(IgniteConstants.IGNITE_CACHE_OPERATION, constant(IgniteCacheOperation.PUT))
                .setHeader(IgniteConstants.IGNITE_CACHE_KEY, ExpressionBuilder.bodyExpression(WaterCollection.class))
                .to(ApacheCamelConfig.WATER_COLLECTION_CACHE_URI);

    }
}
