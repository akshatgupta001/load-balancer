package org.example;

import org.example.utility.LoadBalancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Akshat Gupta
 * Date:07/06/25
 */
public class ClientSocketHandler implements Runnable {
    private final Socket clientSocket;
    private static final LoadBalancer loadBalancer = LoadBalancer.createRoundRobinLoadBalancer(
            Arrays.asList("IP1", "IP2")
    );

    public ClientSocketHandler(final Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream clientToLBInputStream = clientSocket.getInputStream();
            OutputStream lbToClientOutputStream = clientSocket.getOutputStream();

            String backendHost = loadBalancer.getNextServer();
            System.out.println("Host selected to handle this request : " + backendHost);

            //Create a TCP connection with backend server
            Socket backendSocket = new Socket(backendHost, 8080);

            InputStream backendServerToLBInputStream = backendSocket.getInputStream();
            OutputStream lbToBackendServerOutputStream = backendSocket.getOutputStream();

            Thread clientDataHandler = new Thread() {
                @Override
                public void run() {
                    try {
                        int data;
                        while ((data = clientToLBInputStream.read()) != -1) {
                            lbToBackendServerOutputStream.write(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            clientDataHandler.start();

            Thread backendDataHandler = new Thread() {
                @Override
                public void run() {
                    try {
                        int data;
                        while ((data = backendServerToLBInputStream.read()) != -1) {
                            lbToClientOutputStream.write(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            backendDataHandler.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
