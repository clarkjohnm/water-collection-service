package org.cybersapien.watercollection.component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ignite.IgniteConstants;
import org.apache.camel.component.ignite.cache.IgniteCacheOperation;
import org.apache.camel.model.RouteDefinition;
import org.cybersapien.watercollection.config.ApacheCamelConfiguration;
import org.cybersapien.watercollection.config.ApacheIgniteConfiguration;
import org.springframework.stereotype.Component;

/**
 * Workflow for getting existing water collections
 */
@Component
@Slf4j
public class RetrieveWaterCollectionWorkflow extends RouteBuilder implements Processor {

    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + RetrieveWaterCollectionWorkflow.class.getName();

    @Override
    public void configure() throws Exception {
        final String igniteCacheURI = ApacheCamelConfiguration.IGNITE_CACHE_URI_SCHEME + ":"
                + ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_CACHE_NAME + "?"
                + "operation=" + IgniteCacheOperation.GET;

        RouteDefinition worflowDefinition = from(WORKFLOW_URI);
        worflowDefinition.routeId(RetrieveWaterCollectionWorkflow.class.getSimpleName());
        worflowDefinition.setExchangePattern(ExchangePattern.InOut);

        worflowDefinition
                .log("Message received on " + WORKFLOW_URI)
                .setHeader(IgniteConstants.IGNITE_CACHE_KEY, ExpressionBuilder.bodyExpression(String.class))
                .to(igniteCacheURI)
                .process(this);
    }

    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        Message message = exchange.getOut();

        if (null == message) {
            exchange.setException(new NullPointerException("out message is null"));
        }
    }
}
