package org.cybersapien.watercollection.config;

import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * Configuration for Apache Ignite running on GCP
 */
@Configuration
@Profile("gcp")
public class ApacheIgniteGCPConfig {

    /**
     * The TcpDiscoveryKubernetesIpFinder bean
     *
     * @return the TcpDiscoveryKubernetesIpFinder singleton instance
     */
    @Bean(name = "tcpDiscoveryIpFinder")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TcpDiscoveryKubernetesIpFinder tcpDiscoveryKubernetesIpFinder() {
        return new TcpDiscoveryKubernetesIpFinder();
    }
}
