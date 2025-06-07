package org.example.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Main LoadBalancer class that uses the Strategy pattern for different load balancing algorithms
 */
public class LoadBalancer {
    private final List<String> servers;
    private final LoadBalancingStrategy strategy;

    public LoadBalancer(List<String> servers, LoadBalancingStrategy strategy) {
        this.servers = new ArrayList<>(servers);
        this.strategy = strategy;
    }

    public String getNextServer() {
        return strategy.selectServer();
    }

    // Factory method to create a LoadBalancer with RoundRobin strategy
    public static LoadBalancer createRoundRobinLoadBalancer(List<String> servers) {
        return new LoadBalancer(servers, new RoundRobinStrategy(servers));
    }

    // Factory method to create a LoadBalancer with LeastConnections strategy
    public static LoadBalancer createLeastConnectionsLoadBalancer(List<String> servers) {
        return new LoadBalancer(servers, new LeastConnectionsStrategy(servers));
    }
} 