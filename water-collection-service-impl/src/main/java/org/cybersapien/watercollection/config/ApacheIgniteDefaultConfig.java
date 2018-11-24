package org.cybersapien.watercollection.config;

import org.apache.ignite.spi.discovery.tcp.ipfinder.sharedfs.TcpDiscoverySharedFsIpFinder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * Default configuration for Apache Ignite
 */
@Configuration
@Profile("default")
public class ApacheIgniteDefaultConfig {
    /**
     * Directory used for cluster discovery data
     */
    private static final String DISCOVERY_DIRECTORY = "/opt/ignite/addresses";

    /**
     * Discovery finder for a Filesystem
     *
     * @return the filesystem finder
     */
    @Bean(name = "tcpDiscoveryIpFinder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TcpDiscoverySharedFsIpFinder tcpDiscoverySharedFsIpFinder() {
        TcpDiscoverySharedFsIpFinder tcpDiscoverySharedFsIpFinder = new TcpDiscoverySharedFsIpFinder();
        tcpDiscoverySharedFsIpFinder.setPath(DISCOVERY_DIRECTORY);

        return tcpDiscoverySharedFsIpFinder;
    }
}
