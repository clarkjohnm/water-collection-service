package org.cybersapien.watercollection.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.cybersapien.service.water.collection.datatypes.WaterCollection;
import org.cybersapien.watercollection.component.GetWaterCollectionWorkflow;
import org.cybersapien.watercollection.util.ExchangeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for WaterCollection resource
 */
@Slf4j
@RestController
@RequestMapping(path = "/water-collections")
public class WaterCollectionController {

    /**
     * The producer template which starts the workflow
     */
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ProducerTemplate producerTemplate;

    /**
     * Retrieve the water collections
     *
     * @return All water collections
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<WaterCollection> getWaterCollections() {
        List<WaterCollection> result = null;

        Exchange exchange = producerTemplate.send(GetWaterCollectionWorkflow.WORKFLOW_URI, (Processor) null);
        if (null != exchange) {
            //noinspection unchecked
            result = exchange.getProperty(ExchangeConstants.RESPONSE_PROPERTY, List.class);
        }

        return result;
    }
}
