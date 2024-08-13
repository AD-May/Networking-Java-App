import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1080;

    public static void main(String[] args) {
        JFrame frame = new JFrame("TCP Client");
        JButton button = new JButton("Send");
        JTextField textField = new JTextField(20);
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(textField.getText());
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    textArea.append("Received: " + in.readLine() + "\n");
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.getContentPane().add(textField, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
