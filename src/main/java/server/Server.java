package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
	private int port;
	private ServerSocket serverSocket;
	private static Socket newSocket;
	private Map<Socket, PrintWriter> outputStreams = new HashMap<>();

	public Server() throws IOException {
		port = 7777;
		serverSocket = new ServerSocket(port);
		System.err.println("Server started");

		start();
	}

	private void start() throws IOException {
		while (true) {
			newSocket = serverSocket.accept();
			System.out.println("===new client connected===");

			// Creates new client thread
			Thread thread = new Thread(new Runnable() {

				public void run() {
					Socket socket = newSocket;
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter writer = new PrintWriter(socket.getOutputStream());
						outputStreams.put(socket, writer);

						while (socket.isConnected()) {
							String textFromClient = reader.readLine();
							System.out.println(textFromClient + " received");
							sendBroadcast(textFromClient, socket);
						}
					} catch (IOException e) {
						System.out.println("Client disconnected");
						removeFromSavedTreads(socket);
						System.out.println(outputStreams.size());
					}
				}
			});

			thread.start();
		}
	}

	private void sendBroadcast(String message, Socket currentSocket) {
		Iterator<Socket> keys = outputStreams.keySet().iterator();
		while (keys.hasNext()) {
			PrintWriter writer = outputStreams.get(keys.next());
			writer.println(message);
			writer.flush();
		}
	}

	private void removeFromSavedTreads(Socket socket) {
		outputStreams.remove(socket);
	}

	public static void main(String[] args) throws IOException {
		new Server();
	}
}
