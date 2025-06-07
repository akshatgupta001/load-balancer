package org.example.utility;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Least Connections load balancing strategy implementation
 */
public class LeastConnectionsStrategy implements LoadBalancingStrategy {
    private final List<String> servers;
    private final Map<String, AtomicInteger> connectionCounts;

    public LeastConnectionsStrategy(List<String> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("Server list cannot be null or empty");
        }
        this.servers = servers;
        this.connectionCounts = new ConcurrentHashMap<>();
        servers.forEach(server -> connectionCounts.put(server, new AtomicInteger(0)));
    }

    @Override
    public String selectServer() {
        String selectedServer = servers.get(0);
        int minConnections = connectionCounts.get(selectedServer).get();

        for (String server : servers) {
            int currentConnections = connectionCounts.get(server).get();
            if (currentConnections < minConnections) {
                minConnections = currentConnections;
                selectedServer = server;
            }
        }

        connectionCounts.get(selectedServer).incrementAndGet();
        return selectedServer;
    }

    /**
     * Reports a server failure
     *
     * @param server the server that failed
     */
    @Override
    public void reportServerFailure(String server) {

    }

    /**
     * Reports a server success
     *
     * @param server the server that succeeded
     */
    @Override
    public void reportServerSuccess(String server) {

    }

    /**
     * Gets the number of healthy servers
     *
     * @return number of healthy servers
     */
    @Override
    public int getHealthyServerCount() {
        return 0;
    }

    public void releaseConnection(String server) {
        connectionCounts.get(server).decrementAndGet();
    }
} 