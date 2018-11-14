package org.cybersapien.watercollection.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;

import javax.cache.Cache;

/**
 * Processor to read the water collection cache.
 */
@RequiredArgsConstructor
public class WaterCollectionsCacheAdder implements Processor {
    /**
     * Water Collection cache
     */
    private final Cache<String, WaterCollection> waterCollectionCache;

    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        if (exchange.isFailed()) {
            return;
        }

        if (null == exchange.getIn()) {
            exchange.setException(new InvalidProcessorStateException("incoming message is null"));
            return;
        }

        WaterCollection waterCollection = exchange.getIn().getMandatoryBody(WaterCollection.class);
        waterCollectionCache.put(waterCollection.getId(), waterCollection);
    }
}
