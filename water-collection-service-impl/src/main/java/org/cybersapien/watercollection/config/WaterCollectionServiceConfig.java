package org.cybersapien.watercollection.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheReader;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for the water collection service
 */
@RequiredArgsConstructor
@Configuration
public class WaterCollectionServiceConfig {
    /**
     * Ignite
     */
     private final Ignite ignite;

    /**
     * Create NewWaterCollectionPropertiesSetter Processor
     *
     * @return NewWaterCollectionPropertiesSetter Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public NewWaterCollectionPropertiesSetter newWaterCollectionPropertiesSetter() {
        return new NewWaterCollectionPropertiesSetter();
    }

    /**
     * Create WaterCollectionsCacheReader Processor
     *
     * @return WaterCollectionsCacheReader Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WaterCollectionsCacheReader waterCollectionsCacheReader() {
        return new WaterCollectionsCacheReader(ignite);
    }

}
