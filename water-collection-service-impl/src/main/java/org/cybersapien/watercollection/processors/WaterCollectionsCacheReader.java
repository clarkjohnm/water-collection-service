package org.cybersapien.watercollection.processors;

import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.cybersapien.watercollection.config.ApacheIgniteConfig;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;

import javax.cache.Cache;
import javax.inject.Inject;
import java.util.List;

/**
 * Processor to read the water collection cache.
 */
public class WaterCollectionsCacheReader implements Processor {
    @Inject
    private Ignite ignite;

    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        if (exchange.isFailed()) {
            return;
        }

        IgniteCache<String, WaterCollection> waterCollectionCache = ignite.getOrCreateCache(ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME);

        // TODO honor paging in the request. Right now the default page size (1024?) is used.
        List<Cache.Entry<String, WaterCollection>> waterCollectionEntries =
                waterCollectionCache.query(new ScanQuery<String, WaterCollection>()).getAll();

        exchange.getOut().setBody(waterCollectionEntries, List.class);
    }
}
