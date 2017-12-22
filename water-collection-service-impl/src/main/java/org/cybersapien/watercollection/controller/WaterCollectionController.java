package org.cybersapien.watercollection.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.DefaultFluentProducerTemplate;
import org.cybersapien.service.water.collection.datatypes.WaterCollection;
import org.cybersapien.watercollection.component.CreateWaterCollectionWorkflow;
import org.cybersapien.watercollection.component.RetrieveWaterCollectionWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller class for WaterCollection resource. Validation can be added by using the @Valid annotation on
 * method parameters.
 */
@Slf4j
@RestController
@RequestMapping(path = "/water-collections")
public class WaterCollectionController {

    /**
     * The Camel context
     */
    @Autowired
    private CamelContext camelContext;

    /**
     * Get a water collection
     *
     * @param id The id of the water collection
     * @return a water collection instance
     * @throws Exception if an error occurs during processing
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WaterCollection getWaterCollection(@PathVariable Long id) throws Exception {
        WaterCollection result = null;

        FluentProducerTemplate fluentProducerTemplate = new DefaultFluentProducerTemplate(camelContext);
        fluentProducerTemplate.setDefaultEndpointUri(RetrieveWaterCollectionWorkflow.WORKFLOW_URI);

        Exchange exchange = fluentProducerTemplate.withBody(id).send();
        if (null != exchange) {
            if (!exchange.isFailed()) {
                //noinspection unchecked
                result = exchange.getOut().getBody(WaterCollection.class);
            } else {
                throw exchange.getException();
            }
        }

        return result;
    }

    /**
     * Create a water collection so it can be further analyzed
     *
     * @param waterCollection A water collection resource containing the client writable properties according to the JSON contract
     * @return the water collection with properties such as id which are set by the service.
     * @throws Exception if an exception occurred.
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WaterCollection postWaterCollection(@Valid WaterCollection waterCollection) throws Exception {
        WaterCollection result = null;

        FluentProducerTemplate fluentProducerTemplate = new DefaultFluentProducerTemplate(camelContext);
        fluentProducerTemplate.setDefaultEndpointUri(CreateWaterCollectionWorkflow.WORKFLOW_URI);

        Exchange exchange = fluentProducerTemplate.send();
        if (null != exchange) {
            if (!exchange.isFailed()) {
                //noinspection unchecked
                result = exchange.getOut().getBody(WaterCollection.class);
            } else {
                throw exchange.getException();
            }
        }

        return result;
    }
}
