package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private UI UIManager;

	public Client() throws IOException {
		createGUI();
	}

	private void createGUI() {
		UIManager = new UI(new ConnectAction(), new SendAction());
	}

	private boolean setUpConnection() throws IOException {
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		writer.println(UIManager.getName());
		writer.flush();
		String handShakeResult = reader.readLine();
		if (handShakeResult.equals("OK")) {

			Thread thread = new Thread(new Runnable() {

				public void run() {
					while (socket.isConnected()) {
						try {
							String line;
							if ((line = reader.readLine()) != null) {
								UIManager.printOnChatArea(line);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			thread.start();

			return true;
		} else {
			System.out.println("Server Error response");

			return false;
		}

	}

	class SendAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			writer.println(UIManager.getMessage());
			writer.flush();
		}
	}

	class ConnectAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (checkIpAndPort() && checkName()) {
					socket = new Socket(UIManager.getIp(), UIManager.getPort());

					if (socket != null && socket.isConnected()) {
						if (setUpConnection()) {
							System.err.println("Client started");
							UIManager.authorizePanelIsShow(true);
						}
					}
				}
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private boolean checkIpAndPort() {
		// TODO create check IP/PORT logic.
		return true;
	}

	private boolean checkName() {
		if (UIManager.getName() != null && !UIManager.getName().equals("")) {
			return true;
		} else {
			return false;
		}
		// TODO create Name logic.
	}

	public static void main(String[] args) throws IOException {
		new Client();
	}

}
