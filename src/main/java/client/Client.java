package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client {
    private final String SERVER_IP_ADDRESS = "127.0.0.1";
    private final int PORT = 7777;

    private JTextField messageField;
    private Socket socket;
    private PrintWriter writer;

    public Client() throws IOException {
        createGUI();
        setUpConnection();
        System.err.println("Client started");
    }

    private void createGUI() {
        JFrame frame = new JFrame("Simple Chat");
        frame.setLocationRelativeTo(null);
        frame.setSize(new Dimension(400, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        messageField = new JTextField(20);
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
    }

    class SendAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            writer.println(messageField.getText());
            writer.flush();

            messageField.setText("");
            messageField.requestFocus();
        }
    }

}
