package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	private final String SERVER_IP_ADDRESS = "127.0.0.1";
	private final int PORT = 7777;

	private JTextField messageField;
	private JTextArea chatArea;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	public Client() throws IOException {
		createGUI();
		setUpConnection();
		System.err.println("Client started");
	}

	private void createGUI() {
		JFrame frame = new JFrame("Simple Chat");
		frame.setSize(new Dimension(500, 400));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		chatArea = new JTextArea(15, 40);
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		panel.add(scrollPane);

		messageField = new JTextField(40);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendAction());

		panel.add(messageField);
		panel.add(sendButton);

		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}

	private void setUpConnection() throws IOException {
		socket = new Socket(SERVER_IP_ADDRESS, PORT);
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		Thread thread = new Thread(new Runnable() {

			public void run() {
				while (socket.isConnected()) {
					try {
						String line;
						if ((line = reader.readLine()) != null) {
							chatArea.append(line + "\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		thread.start();

	}

	class SendAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			writer.println(messageField.getText());
			writer.flush();

			messageField.setText("");
			messageField.requestFocus();
		}
	}

	public static void main(String[] args) throws IOException {
		new Client();
	}
}
