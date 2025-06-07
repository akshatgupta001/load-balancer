package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class LoadBalancer
{
    public static void main( String[] args ) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8081);
        System.out.println( "Load Balancer started at port : "+8081);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("TCP connection established with client : "+socket.toString());
            handleSocket(socket);
        }
    }
    private static void handleSocket(Socket socket){
        ClientSocketHandler clientSocketHandler = new ClientSocketHandler(socket);
        Thread clientSocketThread  = new Thread(clientSocketHandler);
        clientSocketThread.start();
    }
}
