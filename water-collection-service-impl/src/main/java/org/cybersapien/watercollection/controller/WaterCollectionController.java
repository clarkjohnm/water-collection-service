package org.cybersapien.watercollection.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.FluentProducerTemplate;
import org.cybersapien.watercollection.component.CreateWaterCollectionWorkflow;
import org.cybersapien.watercollection.component.RetrieveWaterCollectionWorkflow;
import org.cybersapien.watercollection.component.RetrieveWaterCollectionsWorkflow;
import org.cybersapien.watercollection.service.v1.api.WaterCollectionResource;
import org.cybersapien.watercollection.service.v1.model.WaterCollection;
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
public class WaterCollectionController implements WaterCollectionResource {
    /**
     * The camel producer template
     */
    private final FluentProducerTemplate fluentProducerTemplate;

    @Override
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<WaterCollection> getWaterCollections() throws WebApplicationException {
        List<WaterCollection> result;

        try {
            //noinspection unchecked
            result = fluentProducerTemplate.to(RetrieveWaterCollectionsWorkflow.WORKFLOW_URI).request(List.class);
        } catch (CamelExecutionException cex) {
            throw new WebApplicationException(cex);
        }

        if (null != result) {
            return result;
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WaterCollection getWaterCollectionById(@PathVariable String id) throws WebApplicationException {
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

    @Override
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public WaterCollection postWaterCollection(@RequestBody @Valid WaterCollection waterCollection) throws WebApplicationException {
        WaterCollection result;

        try {
            result = fluentProducerTemplate.withBody(waterCollection).to(CreateWaterCollectionWorkflow.WORKFLOW_URI).request(WaterCollection.class);
        } catch (CamelExecutionException cex) {
            throw new WebApplicationException(cex);
        }

        return result;
    }
}
