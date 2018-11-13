package org.cybersapien.watercollection.component;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ignite.IgniteConstants;
import org.apache.camel.component.ignite.cache.IgniteCacheOperation;
import org.apache.camel.model.RouteDefinition;
import org.cybersapien.watercollection.config.ApacheCamelConfig;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Workflow for creating water collections.
 * NOTE: A default constructor must be defined for the Camel framework to create this instance of a RouteBuilder. Hence
 * the SuppressWarnings below.
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Slf4j
@Component
@Builder
public class CreateWaterCollectionWorkflow extends RouteBuilder {
    /**
     * The URI of the workflow used by producer templates to start the workflow
     */
    public static final String WORKFLOW_URI = "direct:" + CreateWaterCollectionWorkflow.class.getName();

    /**
     * Processor to set the new water collection properties
     */
    @Inject
    private NewWaterCollectionPropertiesSetter newWaterCollectionPropertiesSetter;

    @Override
    public void configure() throws Exception {
        RouteDefinition worflowDefinition = from(WORKFLOW_URI);
        worflowDefinition.routeId(CreateWaterCollectionWorkflow.class.getSimpleName());
        worflowDefinition.setExchangePattern(ExchangePattern.InOut);

        worflowDefinition
                .log("Message received on " + WORKFLOW_URI)

                .process(newWaterCollectionPropertiesSetter)
                .setHeader(IgniteConstants.IGNITE_CACHE_OPERATION, constant(IgniteCacheOperation.PUT))
                .setHeader(IgniteConstants.IGNITE_CACHE_KEY, simple("${body?.id}"))
                .to(ApacheCamelConfig.WATER_COLLECTION_CACHE_URI);

    }
}
