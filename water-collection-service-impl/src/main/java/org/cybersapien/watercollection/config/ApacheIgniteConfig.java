package org.cybersapien.watercollection.config;

import lombok.NonNull;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Collections;

/**
 * Configuration for Apache Ignite
 */
@Configuration
public class ApacheIgniteConfig {

    /**
     * Cache name for water collections
     */
    public static final String IGNITE_WATER_COLLECTION_CACHE_NAME = WaterCollection.class.getSimpleName();

    /**
     * Indicator for whether or not to use persistent storage
     */
    private final boolean enableFilePersistence = false;

    /**
     * Ignite configuration
     *
     * @return the ignite configuration
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setClientMode(false);

        // durable file memory persistence
        if (enableFilePersistence){
            DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();
            dataStorageConfiguration.setStoragePath("./data/store");
            dataStorageConfiguration.setWalArchivePath("./data/walArchive");
            dataStorageConfiguration.setWalPath("./data/walStore");
            igniteConfiguration.setDataStorageConfiguration(dataStorageConfiguration);
        }

        // connector configuration
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
        connectorConfiguration.setPort(6767);

        // common ignite configuration
        igniteConfiguration.setMetricsLogFrequency(0);
        igniteConfiguration.setQueryThreadPoolSize(2);
        igniteConfiguration.setDataStreamerThreadPoolSize(1);
        igniteConfiguration.setManagementThreadPoolSize(2);
        igniteConfiguration.setPublicThreadPoolSize(2);
        igniteConfiguration.setSystemThreadPoolSize(2);
        igniteConfiguration.setRebalanceThreadPoolSize(1);
        igniteConfiguration.setAsyncCallbackPoolSize(2);
        igniteConfiguration.setPeerClassLoadingEnabled(false);
        igniteConfiguration.setIgniteInstanceName("WaterCollectionGrid");

        BinaryConfiguration binaryConfiguration = new BinaryConfiguration();
        binaryConfiguration.setCompactFooter(false);
        igniteConfiguration.setBinaryConfiguration(binaryConfiguration);

        // logging
        Slf4jLogger slf4jLogger = new Slf4jLogger();
        igniteConfiguration.setGridLogger(slf4jLogger);

        // cluster tcp configuration
        TcpDiscoverySpi tcpDiscoverySpi=new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder=new TcpDiscoveryVmIpFinder();

        // need to be changed when it come to real cluster
        tcpDiscoveryVmIpFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);
        igniteConfiguration.setDiscoverySpi(new TcpDiscoverySpi());

        // cache configuration
        CacheConfiguration waterCollections = new CacheConfiguration();
        waterCollections.setCopyOnRead(false);

        // as we have one node for now
        waterCollections.setBackups(0);
        waterCollections.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        waterCollections.setName(IGNITE_WATER_COLLECTION_CACHE_NAME);
        waterCollections.setIndexedTypes(String.class, WaterCollection.class);

        igniteConfiguration.setCacheConfiguration(waterCollections);
        return igniteConfiguration;
    }

    /**
     * Ignite instance
     * @param igniteConfiguration the ignite configuration
     * @return the ignite instance
     * @throws IgniteException signifying an error occurred
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Ignite ignite(@NonNull IgniteConfiguration igniteConfiguration) throws IgniteException {
        final Ignite bean = Ignition.start(igniteConfiguration);
        bean.active(true);
        return bean;
    }
}
