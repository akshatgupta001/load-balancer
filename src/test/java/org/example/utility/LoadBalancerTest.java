package org.example.utility;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadBalancerTest {
    private final List<String> servers = Arrays.asList("server1", "server2", "server3");

    @Test
    void testRoundRobinLoadBalancer() {
        LoadBalancer loadBalancer = LoadBalancer.createRoundRobinLoadBalancer(servers);

        // First round
        assertEquals("server1", loadBalancer.getNextServer());
        assertEquals("server2", loadBalancer.getNextServer());
        assertEquals("server3", loadBalancer.getNextServer());

        // Second round
        assertEquals("server1", loadBalancer.getNextServer());
        assertEquals("server2", loadBalancer.getNextServer());
        assertEquals("server3", loadBalancer.getNextServer());
    }

    @Test
    void testLeastConnectionsLoadBalancer() {
        LoadBalancer loadBalancer = LoadBalancer.createLeastConnectionsLoadBalancer(servers);

        // First server should be selected as all have 0 connections
        assertEquals("server1", loadBalancer.getNextServer());
        assertEquals("server2", loadBalancer.getNextServer());
    }


    @Test
    void testEmptyServerList() {
        assertThrows(IllegalArgumentException.class, () -> {
            LoadBalancer.createRoundRobinLoadBalancer(Arrays.asList());
        });
    }
} 