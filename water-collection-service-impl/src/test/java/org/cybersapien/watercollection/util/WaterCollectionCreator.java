package org.cybersapien.watercollection.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.cybersapien.watercollection.service.v1.model.WaterCollection;

import java.util.Date;
import java.util.UUID;

/**
 * Creator for WaterCollection instances
 */
public class WaterCollectionCreator {

    /**
     * Build a minimal water collection instance using only required fields
     *
     * @return A minimal instance of WaterCollection
     */
    public static WaterCollection buildMinimal() {
        return new WaterCollection()
                .withStationId(UUID.randomUUID().toString().replaceAll("-",""))
                .withDateTime(new Date())
                .withCollectionVersion(RandomStringUtils.randomNumeric(10))
                .withCollectionType("WELL SAMPLE")
                .withCollectionContent("WATER")
                .withCollectionQuantity(new RandomDataGenerator().nextUniform(10.0, 20.0))
                .withLongitude(new RandomDataGenerator().nextUniform(80.0, 120.0))
                .withLatitude(new RandomDataGenerator().nextUniform(30.0, 50.0));
    }

    public static WaterCollection buildWithMissingRequiredField() {
        WaterCollection waterCollection = buildMinimal();
        waterCollection.setStationId(null);

        return waterCollection;
    }
}
