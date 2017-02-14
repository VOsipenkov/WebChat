

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
	private int port;
	private ServerSocket serverSocket;
	private FileSaver rmiFileService;
	private static Socket newSocket;
	private Map<Socket, PrintWriter> outputStreams = new HashMap<>();

	public Server() throws IOException {
		port = 7777;
		serverSocket = new ServerSocket(port);
		System.err.println("Server started");
		
		try {
			Registry reg = LocateRegistry.getRegistry("127.0.0.1", 9999);
			rmiFileService = (FileSaver) reg.lookup("toFile");
			if (rmiFileService != null) {
				System.err.println("connected to remote db service successfull");
			}
		} catch (NotBoundException e) {
			System.out.println("Error throws when try connect to dv service");
			e.printStackTrace();
		}

		if (rmiFileService == null) {
			System.err.println("remote db service not found");
		}
		
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

						String clientName = reader.readLine();
						if (clientName != null && !clientName.equals("")) {
							writer.println("OK");
							writer.flush();
						}

						while (socket.isConnected()) {
							String textFromClient = reader.readLine();
							System.out.println(textFromClient + " received");
							sendBroadcast(clientName, textFromClient, socket);
							if (rmiFileService != null) {
								rmiFileService.add(clientName, textFromClient);
							}
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

	private void sendBroadcast(String author, String message, Socket currentSocket) {
		Iterator<Socket> keys = outputStreams.keySet().iterator();
		while (keys.hasNext()) {
			PrintWriter writer = outputStreams.get(keys.next());
			writer.println(author + ": " + message);
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
