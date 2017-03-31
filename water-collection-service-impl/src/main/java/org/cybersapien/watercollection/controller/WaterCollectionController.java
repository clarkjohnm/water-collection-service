package org.cybersapien.watercollection.controller;

import org.cybersapien.service.water.collection.datatypes.WaterCollection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for WaterCollection resource
 */
@RestController
public class WaterCollectionController {

    /**
     * Retrieve the water collections
     *
     * @return All water collections
     */
    @RequestMapping(method = RequestMethod.GET, path = "/water-collections", produces = "application/json")
    public List<WaterCollection> getWaterCollections() {
        return new ArrayList<>();
    }
}
