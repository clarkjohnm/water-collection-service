package org.cybersapien.watercollection.component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.cybersapien.service.water.collection.datatypes.WaterCollection;
import org.cybersapien.watercollection.util.ExchangeConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Workflow for getting existing water collections
 */
@Component
@Slf4j
public class GetWaterCollectionWorkflow extends RouteBuilder implements Processor {

    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + GetWaterCollectionWorkflow.class.getName();

    @Override
    public void configure() throws Exception {
        from(WORKFLOW_URI)
                .log("Message received on " + WORKFLOW_URI)
                .process(this);
    }

    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        exchange.setProperty(ExchangeConstants.RESPONSE_PROPERTY, new ArrayList<WaterCollection>());
    }
}
