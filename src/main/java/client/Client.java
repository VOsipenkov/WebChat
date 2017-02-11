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
	private JTextField nameField;
	private JTextField ipText;
	private JTextField portText;

	public Client() throws IOException {
		createGUI();
	}

	private void createGUI() {
		JFrame frame = new JFrame("Simple Chat");
		frame.setSize(new Dimension(500, 350));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		authorizePanel = new JPanel();
		JLabel nameLabel = new JLabel("Your name");
		nameField = new JTextField(10);
		JLabel ipLabel = new JLabel("IP");
		ipText = new JTextField(5);
		JLabel portLabel = new JLabel("Port");
		portText = new JTextField(3);
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

		ipText.setText(SERVER_IP_ADDRESS);
		portText.setText(Integer.toString(PORT));
	}

	private boolean setUpConnection() throws IOException {
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		writer.println(nameField);
		writer.flush();
		String handShakeResult = reader.readLine();
		if (handShakeResult.equals("OK")) {

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

			return true;
		} else {
			System.out.println("Server Error response");

			return false;
		}

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
			try {
				if (checkIpAndPort() && checkName()) {
					socket = new Socket(ipText.getText().trim(), Integer.parseInt(portText.getText().trim()));

					if (socket != null && socket.isConnected()) {
						if (setUpConnection()) {
							System.err.println("Client started");
							chatPanel.setVisible(true);
							authorizePanel.setVisible(false);
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
		if (nameField.getText() != null && !nameField.equals("")) {
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
