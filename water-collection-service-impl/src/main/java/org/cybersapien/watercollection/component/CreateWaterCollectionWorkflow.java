package org.cybersapien.watercollection.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheAdder;
import org.springframework.stereotype.Component;

/**
 * Workflow for creating a water collection.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateWaterCollectionWorkflow extends RouteBuilder {
    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + CreateWaterCollectionWorkflow.class.getName();

    /**
     * Processor to set the new water collection properties
     */
    private final NewWaterCollectionPropertiesSetter newWaterCollectionPropertiesSetter;

    /**
     * Processor to add the new water collection
     */
    private final WaterCollectionsCacheAdder waterCollectionsCacheAdder;

    @Override
    public void configure() throws Exception {
        RouteDefinition worflowDefinition = from(WORKFLOW_URI);
        worflowDefinition.routeId(CreateWaterCollectionWorkflow.class.getSimpleName());
        worflowDefinition.setExchangePattern(ExchangePattern.InOut);

        worflowDefinition
                .log("Message received on " + WORKFLOW_URI)
                .process(newWaterCollectionPropertiesSetter)
                .process(waterCollectionsCacheAdder);
    }
}
