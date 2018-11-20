package org.cybersapien.watercollection.config;

import lombok.RequiredArgsConstructor;
import org.cybersapien.watercollection.processors.NewWaterCollectionPropertiesSetter;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheAdder;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheBulkReader;
import org.cybersapien.watercollection.processors.WaterCollectionsCacheReader;
import org.cybersapien.watercollection.service.v1.model.WaterCollection;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.cache.Cache;

/**
 * Configuration for the water collection service
 */
@RequiredArgsConstructor
@Configuration
public class WaterCollectionServiceConfig {
    /**
     * Water Collection cache
     */
    private final Cache<String, WaterCollection> waterCollectionCache;

    /**
     * Get NewWaterCollectionPropertiesSetter Processor
     *
     * @return NewWaterCollectionPropertiesSetter Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public NewWaterCollectionPropertiesSetter newWaterCollectionPropertiesSetter() {
        return new NewWaterCollectionPropertiesSetter();
    }

    /**
     * Get WaterCollectionsCacheAdder Processor
     *
     * @return WaterCollectionsCacheAdder Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WaterCollectionsCacheAdder waterCollectionsCacheAdder() {
        return new WaterCollectionsCacheAdder(waterCollectionCache);
    }

    /**
     * Get WaterCollectionsCacheReader Processor
     *
     * @return WaterCollectionsCacheReader Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WaterCollectionsCacheReader waterCollectionsCacheReader() {
        return new WaterCollectionsCacheReader(waterCollectionCache);
    }

    /**
     * Get WaterCollectionsCacheBulkReader Processor
     *
     * @return WaterCollectionsCacheBulkReader Processor
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WaterCollectionsCacheBulkReader WaterCollectionsCacheBulkReader() {
        return new WaterCollectionsCacheBulkReader(waterCollectionCache);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/api/**")
                        .addResourceLocations("classpath:/api/");
            }
        };
    }
}
