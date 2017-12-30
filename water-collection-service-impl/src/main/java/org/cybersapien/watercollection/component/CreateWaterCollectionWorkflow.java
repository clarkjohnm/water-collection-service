package org.cybersapien.watercollection.component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ignite.IgniteConstants;
import org.apache.camel.component.ignite.cache.IgniteCacheEndpoint;
import org.apache.camel.component.ignite.cache.IgniteCacheOperation;
import org.apache.camel.component.ignite.idgen.IgniteIdGenEndpoint;
import org.apache.camel.component.ignite.idgen.IgniteIdGenOperation;
import org.apache.camel.model.RouteDefinition;
import org.springframework.stereotype.Component;

/**
 * Workflow for creating water collections
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CreateWaterCollectionWorkflow extends RouteBuilder implements Processor {

    /**
     * The ignite cache endpoint
     */
    private final IgniteCacheEndpoint igniteCacheEndpoint;

    /**
     * The ignite idgen endpoint
     */
    private final IgniteIdGenEndpoint igniteIdGenEndpoint;

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
                .setHeader(IgniteConstants.IGNITE_IDGEN_OPERATION, constant(IgniteIdGenOperation.GET_AND_INCREMENT))
                .to(igniteIdGenEndpoint)
                .setHeader(IgniteConstants.IGNITE_CACHE_OPERATION, constant(IgniteCacheOperation.PUT))
                .setHeader(IgniteConstants.IGNITE_CACHE_KEY, ExpressionBuilder.bodyExpression(String.class))
                .to(igniteCacheEndpoint)
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
