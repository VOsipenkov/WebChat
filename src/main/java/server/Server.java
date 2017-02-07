package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        System.err.println("Server started");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("===new client connected===");
            while (socket.isConnected()) {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    System.out.println(reader.readLine() + " received");
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                    break;
                }
            }
        }
    }
}
