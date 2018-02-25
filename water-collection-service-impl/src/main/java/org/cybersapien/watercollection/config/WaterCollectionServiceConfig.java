package org.cybersapien.watercollection.config;

import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for the water collection service
 */
@Configuration
public class WaterCollectionServiceConfig {
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

}
