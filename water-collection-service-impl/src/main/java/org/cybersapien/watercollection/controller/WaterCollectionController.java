package org.cybersapien.watercollection.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.FluentProducerTemplate;
import org.cybersapien.watercollection.component.CreateWaterCollectionWorkflow;
import org.cybersapien.watercollection.component.RetrieveWaterCollectionWorkflow;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
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
import java.util.List;

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
     * @return a list of water collection instances
     * @throws Exception if an error occurs during processing
     */
    @ApiOperation(value = "Get a list of water collections",
            notes = "By default, the last 10 submitted water collections are returned")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<WaterCollection> getWaterCollections() throws Exception {
        List<WaterCollection> result;

        try {
            result = fluentProducerTemplate.to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(List.class);
        } catch (CamelExecutionException cex) {
            throw new WebApplicationException(cex);
        }

        if (null != result) {
            return result;
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    /**
     * Get a water collection
     *
     * @param id The id of the water collection
     * @return a water collection instance
     * @throws Exception if an error occurs during processing
     */
    @ApiOperation(value = "Get a water collection by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public WaterCollection getWaterCollection(@PathVariable String id) throws Exception {
        WaterCollection result;

        try {
            result = fluentProducerTemplate.withBody(id).to(RetrieveWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);
        } catch (CamelExecutionException cex) {
            throw new WebApplicationException(cex);
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
    @ApiOperation(value = "Create a water collection")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public WaterCollection postWaterCollection(@RequestBody @Valid WaterCollection waterCollection) throws Exception {
        WaterCollection result;

        try {
            result = fluentProducerTemplate.withBody(waterCollection).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);
        } catch (CamelExecutionException cex) {
            throw new WebApplicationException(cex);
        }

        return result;
    }
}
