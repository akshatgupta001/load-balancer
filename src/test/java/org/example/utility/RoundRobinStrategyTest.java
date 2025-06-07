package org.example.utility;

import org.example.exception.LoadBalancerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinStrategyTest {
    private RoundRobinStrategy strategy;
    private List<String> servers;

    @BeforeEach
    void setUp() {
        servers = new ArrayList<>(Arrays.asList("server1", "server2", "server3"));
        strategy = new RoundRobinStrategy(servers);
    }

    @Test
    void testRoundRobinDistribution() throws LoadBalancerException {
        // First round
        assertEquals("server1", strategy.selectServer());
        assertEquals("server2", strategy.selectServer());
        assertEquals("server3", strategy.selectServer());

        // Second round
        assertEquals("server1", strategy.selectServer());
        assertEquals("server2", strategy.selectServer());
        assertEquals("server3", strategy.selectServer());
    }

    @Test
    void testWithSingleServer() throws LoadBalancerException {
        List<String> singleServer = new ArrayList<>(Arrays.asList("server1"));
        RoundRobinStrategy singleStrategy = new RoundRobinStrategy(singleServer);

        assertEquals("server1", singleStrategy.selectServer());
        assertEquals("server1", singleStrategy.selectServer());
    }

    @Test
    void testWithEmptyServerList() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RoundRobinStrategy(new ArrayList<>());
        });
        assertEquals("Server list cannot be null or empty", exception.getMessage());
    }

    @Test
    void testWithNullServerList() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RoundRobinStrategy(null);
        });
        assertEquals("Server list cannot be null or empty", exception.getMessage());
    }

    @Test
    void testServerFailure() throws LoadBalancerException {
        // Initially all servers are available
        assertEquals(3, strategy.getHealthyServerCount());

        // Report failure for server1
        strategy.reportServerFailure("server1");
        assertEquals(2, strategy.getHealthyServerCount());

        // Verify server1 is not selected
        String selectedServer = strategy.selectServer();
        assertNotEquals("server1", selectedServer);
    }

    @Test
    void testServerRecovery() throws LoadBalancerException {
        // Report failure for server1
        strategy.reportServerFailure("server1");
        assertEquals(2, strategy.getHealthyServerCount());

        // Report success for server1
        strategy.reportServerSuccess("server1");
        assertEquals(3, strategy.getHealthyServerCount());

        // Verify server1 can be selected again
        boolean server1Selected = false;
        for (int i = 0; i < 3; i++) {
            if (strategy.selectServer().equals("server1")) {
                server1Selected = true;
                break;
            }
        }
        assertTrue(server1Selected, "Server1 should be selected after being marked healthy");
    }

    @Test
    void testNoServersAvailable() {
        // Remove all servers
        servers.clear();
        
        // Should throw exception when no servers available
        LoadBalancerException exception = assertThrows(LoadBalancerException.class, () -> {
            strategy.selectServer();
        });
        assertEquals("No servers available", exception.getMessage());
    }
} 