package client;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client {
	private final String SERVER_IP_ADDRESS = "127.0.0.1";
	private final int PORT = 7777;

	private JTextField messageField;
	private JTextArea chatArea;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private JPanel chatPanel;
	private JPanel authorizePanel;

	public Client() throws IOException {
		createGUI();
		setUpConnection();
		System.err.println("Client started");
	}

	private void createGUI() {
		JFrame frame = new JFrame("Simple Chat");
		frame.setSize(new Dimension(500, 350));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		authorizePanel = new JPanel();
		JLabel nameLabel = new JLabel("Your name");
		JTextField nameField = new JTextField(10);
		JLabel ipLabel = new JLabel("IP");
		JTextField ipText = new JTextField(3);
		JLabel portLabel = new JLabel("Port");
		JTextField portText = new JTextField(3);
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ConnectAction());

		authorizePanel.add(nameLabel);
		authorizePanel.add(nameField);
		authorizePanel.add(ipLabel);
		authorizePanel.add(ipText);
		authorizePanel.add(portLabel);
		authorizePanel.add(portText);
		authorizePanel.add(connectButton);

		chatPanel = new JPanel();
		chatArea = new JTextArea(15, 40);
		chatArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatPanel.add(scrollPane);

		messageField = new JTextField(40);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendAction());

		chatPanel.add(messageField);
		chatPanel.add(sendButton);
		chatPanel.setVisible(false);

		frame.getContentPane().add(authorizePanel, BorderLayout.NORTH);
		frame.getContentPane().add(chatPanel, BorderLayout.CENTER);
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

	class ConnectAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			chatPanel.setVisible(true);
			authorizePanel.setVisible(false);
		}

	}

	public static void main(String[] args) throws IOException {
		new Client();
	}

}
