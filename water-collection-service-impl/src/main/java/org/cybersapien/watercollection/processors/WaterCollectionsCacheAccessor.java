package org.cybersapien.watercollection.processors;

import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import java.util.List;

/**
 * Processor to set the properties of a new water collection which must be set by the server.
 */
public class WaterCollectionsCacheAccessor implements Processor {
    /** WaterCollection cache */
    @Autowired
    private IgniteCache<String, WaterCollection> waterCollectionCache;

    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        if (exchange.isFailed()) {
            return;
        }

        List<Cache.Entry<String, WaterCollection>> waterCollectionEntries =
                waterCollectionCache.query(new ScanQuery<String, WaterCollection>()).getAll();

        exchange.getOut().setBody(waterCollectionEntries, List.class);
    }
}
