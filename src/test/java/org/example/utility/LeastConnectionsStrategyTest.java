package org.example.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeastConnectionsStrategyTest {
    private LeastConnectionsStrategy strategy;
    private List<String> servers;

    @BeforeEach
    void setUp() {
        servers = Arrays.asList("server1", "server2", "server3");
        strategy = new LeastConnectionsStrategy(servers);
    }

    @Test
    void testInitialDistribution() {
        // First server should be selected as all have 0 connections
        assertEquals("server1", strategy.selectServer());
    }

    @Test
    void testLeastConnectionsSelection() {
        // Select server1 first
        String server1 = strategy.selectServer();
        assertEquals("server1", server1);

        // Select server2
        String server2 = strategy.selectServer();
        assertEquals("server2", server2);

        // Release connection from server1
        strategy.releaseConnection(server1);

        // Next selection should be server1 as it has 0 connections
        assertEquals("server1", strategy.selectServer());
    }

    @Test
    void testWithSingleServer() {
        List<String> singleServer = Arrays.asList("server1");
        LeastConnectionsStrategy singleStrategy = new LeastConnectionsStrategy(singleServer);

        assertEquals("server1", singleStrategy.selectServer());
        assertEquals("server1", singleStrategy.selectServer());
    }

    @Test
    void testConnectionRelease() {
        String server = strategy.selectServer();
        strategy.releaseConnection(server);
        // After release, the same server should be selected again
        assertEquals(server, strategy.selectServer());
    }
} 