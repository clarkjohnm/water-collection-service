package org.cybersapien.watercollection.config;

import lombok.NonNull;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.cybersapien.watercollection.service.datatypes.v1.service.WaterCollection;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${ignite.persistence}")
    private boolean enableFilePersistence;

    /**
     * Ignite configuration
     *
     * @return the ignite configuration
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        // durable file memory persistence
        DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();
        dataStorageConfiguration.getDefaultDataRegionConfiguration().setPersistenceEnabled(enableFilePersistence);
        if (enableFilePersistence){
            dataStorageConfiguration.setStoragePath("/opt/ignite/data/store");
            dataStorageConfiguration.setWalArchivePath("/opt/ignite/data/walArchive");
            dataStorageConfiguration.setWalPath("/opt/ignite/data/walStore");
        }
        igniteConfiguration.setDataStorageConfiguration(dataStorageConfiguration);

        // connector configuration for receiving REST requests
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
        connectorConfiguration.setPort(6767);
        igniteConfiguration.setConnectorConfiguration(connectorConfiguration);

        // ignite pool configurations
        igniteConfiguration.setQueryThreadPoolSize(2);
        igniteConfiguration.setDataStreamerThreadPoolSize(1);
        igniteConfiguration.setManagementThreadPoolSize(2);
        igniteConfiguration.setPublicThreadPoolSize(2);
        igniteConfiguration.setSystemThreadPoolSize(2);
        igniteConfiguration.setRebalanceThreadPoolSize(1);
        igniteConfiguration.setAsyncCallbackPoolSize(2);

        // logging
        Slf4jLogger slf4jLogger = new Slf4jLogger();
        igniteConfiguration.setGridLogger(slf4jLogger);

        // cluster tcp configuration
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder();
        // This configuration allows up to 10 nodes. This setting needs to be considered for changing for real cluster
        // TODO Determine how to configure discovery for a cloud environment.
        tcpDiscoveryVmIpFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setClientReconnectDisabled(true);
        tcpDiscoverySpi.setForceServerMode(true);
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryVmIpFinder);

        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);

        // Set networking parameters such as connect and read timeouts, tcpNoDelay, etc.
        TcpCommunicationSpi tcpCommunicationSpi = new TcpCommunicationSpi();
        tcpCommunicationSpi.setLocalPort(47100);
        tcpCommunicationSpi.setLocalPortRange(100);

        igniteConfiguration.setCommunicationSpi(tcpCommunicationSpi);

        // cache configuration
        CacheConfiguration waterCollections = new CacheConfiguration();
        waterCollections.setCopyOnRead(false);

        // Set backups to 0 since we have one node for now
        waterCollections.setBackups(0);
        waterCollections.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        waterCollections.setName(IGNITE_WATER_COLLECTION_CACHE_NAME);
        //noinspection unchecked
        waterCollections.setIndexedTypes(String.class, WaterCollection.class);

        igniteConfiguration.setCacheConfiguration(waterCollections);

        // misc configuration
        igniteConfiguration.setMetricsLogFrequency(0);
        igniteConfiguration.setPeerClassLoadingEnabled(false);
        igniteConfiguration.setClientMode(false);

        igniteConfiguration.setIgniteInstanceName("WaterCollectionGrid");

        return igniteConfiguration;
    }

    /**
     * Create WaterCollection Cache
     *
     * @return WaterCollection cache
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public IgniteCache<String, WaterCollection> waterCollectionCache() {
        return ignite(igniteConfiguration()).getOrCreateCache(ApacheIgniteConfig.IGNITE_WATER_COLLECTION_CACHE_NAME);
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
        Ignite bean = Ignition.getOrStart(igniteConfiguration);
        bean.active(true);

        return bean;
    }
}
