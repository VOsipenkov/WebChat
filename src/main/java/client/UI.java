package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import client.Client.ConnectAction;
import client.Client.SendAction;

public class UI {
	private final String SERVER_IP_ADDRESS = "127.0.0.1";
	private final int PORT = 7777;
	private JTextField messageField;
	private JTextArea chatArea;
	private JPanel chatPanel;
	private JPanel authorizePanel;
	private JTextField nameField;
	private JTextField ipText;
	private JTextField portText;

	public UI(ConnectAction connectionAction, SendAction sendAction) {
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
		connectButton.addActionListener(connectionAction);

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
		sendButton.addActionListener(sendAction);

		chatPanel.add(messageField);
		chatPanel.add(sendButton);
		chatPanel.setVisible(false);

		frame.getContentPane().add(authorizePanel, BorderLayout.NORTH);
		frame.getContentPane().add(chatPanel, BorderLayout.CENTER);
		frame.setVisible(true);

		ipText.setText(SERVER_IP_ADDRESS);
		portText.setText(Integer.toString(PORT));
	}

	public String getName() {
		return nameField.getText().trim();
	}

	public void printOnChatArea(String s) {
		chatArea.append(s + "\n");
	}

	public String getMessage() {
		String message = messageField.getText().trim();
		messageField.setText("");
		messageField.requestFocus();

		return message;
	}

	public void authorizePanelIsShow(boolean b) {
		chatPanel.setVisible(b);
		authorizePanel.setVisible(!b);
	}

	public String getIp() {
		return ipText.getText().trim();
	}

	public int getPort() {
		return Integer.parseInt(portText.getText().trim());
	}
}
