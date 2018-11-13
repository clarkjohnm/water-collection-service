package org.cybersapien.watercollection.processors;

import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;

import java.util.UUID;

/**
 * Processor to set the properties of a new water collection which must be set by the server.
 */
public class NewWaterCollectionPropertiesSetter implements Processor {
    @Override
    public void process(@NonNull Exchange exchange) throws Exception {
        if (exchange.isFailed()) {
            return;
        }

        if (null == exchange.getIn()) {
            exchange.setException(new InvalidProcessorStateException("incoming message is null"));
            return;
        }

        WaterCollection waterCollection = exchange.getIn().getBody(WaterCollection.class);
        if (null == waterCollection) {
            exchange.setException(new InvalidProcessorStateException("WaterCollection is null"));
            return;
        }

        waterCollection.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        waterCollection.setProcessingState(ProcessingState.NOT_STARTED.name());
    }
}
