package org.cybersapien.watercollection.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;

import javax.cache.Cache;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Processor to read the water collection cache.
 */
@Named
@RequiredArgsConstructor
public class WaterCollectionsCacheBulkReader implements Processor {
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

        // TODO honor paging in the request. Right now the default page size (1024?) is used.
        List<WaterCollection> waterCollections = new ArrayList<>();
        for (Cache.Entry<String, WaterCollection> entry : waterCollectionCache) {
            waterCollections.add(entry.getValue());
        }
        exchange.getOut().setBody(waterCollections, List.class);
    }
}
