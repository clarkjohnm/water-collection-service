package org.cybersapien.watercollection.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.cybersapien.service.water.collection.datatypes.v1.service.WaterCollection;
import org.cybersapien.watercollection.component.CreateWaterCollectionWorkflow;
import org.cybersapien.watercollection.component.RetrieveWaterCollectionWorkflow;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

/**
 * Controller class for WaterCollection resource. Validation can be added by using the @Valid annotation on
 * method parameters.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/water-collections")
public class WaterCollectionController {

    /**
     * The camel producer template
     */
    private final FluentProducerTemplate fluentProducerTemplate;

    /**
     * Get a water collection
     *
     * @param id The id of the water collection
     * @return a water collection instance
     * @throws Exception if an error occurs during processing
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public WaterCollection getWaterCollection(@PathVariable Long id) throws Exception {
        WaterCollection result = null;

        Exchange exchange = fluentProducerTemplate.withBody(id).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).send();
        if (null != exchange) {
            if (!exchange.isFailed()) {
                //noinspection unchecked
                result = exchange.getOut().getBody(WaterCollection.class);
            } else {
                throw new WebApplicationException(exchange.getException());
            }
        }

        if (null != result) {
            return result;
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    /**
     * Create a water collection so it can be further analyzed
     *
     * @param waterCollection A water collection resource containing the client writable properties according to the JSON contract
     * @return the water collection with properties such as id which are set by the service.
     * @throws Exception if an exception occurred.
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public WaterCollection postWaterCollection(@RequestBody @Valid WaterCollection waterCollection) throws Exception {
        WaterCollection result = null;

        Exchange exchange = fluentProducerTemplate.withBody(waterCollection).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).send();
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
