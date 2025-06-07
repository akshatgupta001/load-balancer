package org.example.utility;

import org.example.exception.LoadBalancerException;

/**
 * Interface defining the contract for load balancing strategies
 */
public interface LoadBalancingStrategy {
    /**
     * Selects a server from the available backend servers
     * @return the selected server's host
     * @throws LoadBalancerException if no healthy servers are available
     */
    String selectServer() throws LoadBalancerException;

    /**
     * Reports a server failure
     * @param server the server that failed
     */
    void reportServerFailure(String server);

    /**
     * Reports a server success
     * @param server the server that succeeded
     */
    void reportServerSuccess(String server);

    /**
     * Gets the number of healthy servers
     * @return number of healthy servers
     */
    int getHealthyServerCount();
} 