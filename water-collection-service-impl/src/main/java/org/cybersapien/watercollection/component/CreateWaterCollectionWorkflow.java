package org.cybersapien.watercollection.component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ignite.cache.IgniteCacheOperation;
import org.apache.camel.component.ignite.idgen.IgniteIdGenOperation;
import org.cybersapien.watercollection.config.ApacheCamelConfiguration;
import org.cybersapien.watercollection.config.ApacheIgniteConfiguration;
import org.springframework.stereotype.Component;

/**
 * Workflow for creating water collections
 */
@Component
@Slf4j
public class CreateWaterCollectionWorkflow extends RouteBuilder implements Processor {

    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + CreateWaterCollectionWorkflow.class.getName();

    @Override
    public void configure() throws Exception {
        final String igniteCacheURI = ApacheCamelConfiguration.IGNITE_CACHE_URI_SCHEME + ":"
                + ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_CACHE_NAME + "?"
                + "operation=" + IgniteCacheOperation.PUT;

        final String igniteIdGenURI = ApacheCamelConfiguration.IGNITE_IDGEN_URI_SCHEME + ":"
                + ApacheIgniteConfiguration.IGNITE_WATER_COLLECTION_SEQUENCE_NAME + "?"
                + "operation=" + IgniteIdGenOperation.GET_AND_INCREMENT;

        from(WORKFLOW_URI)
                .routeId(CreateWaterCollectionWorkflow.class.getSimpleName())
                .setExchangePattern(ExchangePattern.InOut)
                .log("Message received on " + WORKFLOW_URI)
                .to(igniteIdGenURI)
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
