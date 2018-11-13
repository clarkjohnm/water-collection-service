package org.cybersapien.watercollection.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheReader;
import org.springframework.stereotype.Component;

/**
 * Workflow for getting existing water collections
 * NOTE: A default constructor must be defined for the Camel framework to create this instance of a RouteBuilder. Hence
 * the SuppressWarnings below.
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@RequiredArgsConstructor
@Slf4j
@Component
public class RetrieveWaterCollectionsWorkflow extends RouteBuilder {
    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + RetrieveWaterCollectionsWorkflow.class.getName();

    /**
     * Processor to read the water collection cache
     */
    private final WaterCollectionsCacheReader waterCollectionsCacheReader;

    @Override
    public void configure() throws Exception {
        RouteDefinition worflowDefinition = from(WORKFLOW_URI);
        worflowDefinition.routeId(RetrieveWaterCollectionsWorkflow.class.getSimpleName());
        worflowDefinition.setExchangePattern(ExchangePattern.InOut);

        worflowDefinition
                .log("Message received on " + WORKFLOW_URI)
                .process(waterCollectionsCacheReader);
    }
}
