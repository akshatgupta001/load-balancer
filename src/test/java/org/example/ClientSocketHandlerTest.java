package org.example;

import org.example.utility.LoadBalancer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientSocketHandlerTest {

    @Mock
    private Socket clientSocket;
    
    @Mock
    private Socket backendSocket;
    
    @Mock
    private LoadBalancer loadBalancer;

    private ClientSocketHandler handler;
    private ByteArrayInputStream clientInputStream;
    private ByteArrayOutputStream clientOutputStream;
    private ByteArrayInputStream backendInputStream;
    private ByteArrayOutputStream backendOutputStream;

    @BeforeEach
    void setUp() throws IOException {
        // Setup test data
        clientInputStream = new ByteArrayInputStream("test data".getBytes());
        clientOutputStream = new ByteArrayOutputStream();
        backendInputStream = new ByteArrayInputStream("response data".getBytes());
        backendOutputStream = new ByteArrayOutputStream();

        // Setup mock behavior
        when(clientSocket.getInputStream()).thenReturn(clientInputStream);
        when(clientSocket.getOutputStream()).thenReturn(clientOutputStream);
        when(backendSocket.getInputStream()).thenReturn(backendInputStream);
        when(backendSocket.getOutputStream()).thenReturn(backendOutputStream);

        handler = new ClientSocketHandler(clientSocket);
    }

    @Test
    void testDataTransfer() throws IOException, InterruptedException {
        // Start the handler in a separate thread
        Thread handlerThread = new Thread(handler);
        handlerThread.start();

        // Wait for a short time to allow data transfer
        Thread.sleep(100);

        // Verify that data was transferred
        assertTrue(clientOutputStream.toString().contains("response data"));
    }

    @Test
    void testClientSocketClosed() throws IOException {
        // Simulate client socket closure
        when(clientSocket.getInputStream()).thenThrow(new IOException("Connection closed"));

        // Run the handler
        handler.run();

        // Verify that the handler handles the exception gracefully
        verify(clientSocket, times(1)).getInputStream();
    }

    @Test
    void testBackendSocketClosed() throws IOException {
        // Simulate backend socket closure
        when(backendSocket.getInputStream()).thenThrow(new IOException("Connection closed"));

        // Run the handler
        handler.run();

        // Verify that the handler handles the exception gracefully
        verify(backendSocket, times(1)).getInputStream();
    }
} 