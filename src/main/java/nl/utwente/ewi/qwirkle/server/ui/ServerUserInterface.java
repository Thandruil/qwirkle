package nl.utwente.ewi.qwirkle.server.ui;

import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.model.PlayerList;
import nl.utwente.ewi.qwirkle.util.Logger;
import nl.utwente.ewi.qwirkle.util.TextAreaOutputStream;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a screen to monitor the server status. This includes the clients connected and their states, if the server is online and the logs of the server.
 */
public class ServerUserInterface extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    private JLabel statusText;
    private JLabel ipText;
    private JLabel portText;

    private JTextArea logArea;

    private DefaultListModel<String> clientList;

    private boolean status;

    /**
     * Created the elements and adds them to the frame. The left, mid and right panels are the main vertical panels whithin the frame.
     */
    public ServerUserInterface() {

        mainPanel = new JPanel(new GridLayout(1,3));
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        statusText = new JLabel();
        statusText.setFont(new Font("Arial", Font.BOLD, 20));
        setStatus(false);
        leftPanel.add(statusText);

        ipText = new JLabel();
        ipText.setFont(new Font("Arial", Font.PLAIN, 12));
        leftPanel.add(ipText);

        portText = new JLabel();
        portText.setFont(new Font("Arial", Font.PLAIN, 12));
        leftPanel.add(portText);

        JLabel clientsText = new JLabel();
        clientsText.setFont(new Font("Arial", Font.BOLD, 18));
        clientsText.setText("Clients");
        leftPanel.add(clientsText);

        clientList = new DefaultListModel<>();
        JList clientListField = new JList<>(clientList);
        clientListField.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        clientListField.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        clientListField.setVisibleRowCount(-1);
        leftPanel.add(clientListField);

        logArea = new JTextArea();
        Logger.addOutputStream(Logger.INFO, new TextAreaOutputStream(logArea));
        logArea.setPreferredSize(new Dimension(mainPanel.getWidth()/3, mainPanel.getHeight()));
        //midPanel.add(logArea);


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.add(mainPanel);
        mainPanel.add(leftPanel);
        mainPanel.add(logArea);
        mainPanel.add(rightPanel);
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    /**
     * Sets the status in the User Interface of the server.
     * @param s Should be TRUE for 'Online' and FALSE for 'Offline'.
     */
    public void setStatus(boolean s) {
        if (s) {
            statusText.setForeground(Color.GREEN);
            statusText.setText("Online");
        } else {
            statusText.setForeground(Color.RED);
            statusText.setText("Offline");
        }
    }

    /**
     * Sets the IP of the server in the User Interface.
     * @param ip The IP of the server as a String.
     */
    public void setIp(String ip) {
        ipText.setText("IP: " + ip);
    }

    /**
     * Sets the port the server is listening on in the User Interface.
     * @param port The port the server is listening on as a String.
     */
    public void setPort(String port) {
        portText.setText("Port: " + port);
    }

    /**
     * Updates the connected clients and their states.
     */
    public void updateClients() {
        clientList.clear();
        for (ClientHandler ch : PlayerList.getPlayerList().values()) {
            clientList.addElement(ch.getName() + " (" + ch.getState() + ")");
        }
    }
}
