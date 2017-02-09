package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port;
	private ServerSocket serverSocket;

	public Server() throws IOException {
		port = 7777;
		serverSocket = new ServerSocket(port);
		System.err.println("Server started");

		start();
	}

	private void start() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("===new client connected===");
			while (socket.isConnected()) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String textFromClient = reader.readLine();
					System.out.println(textFromClient + " received");
				} catch (IOException e) {
					System.out.println("Client disconnected");
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Server();
	}
}
