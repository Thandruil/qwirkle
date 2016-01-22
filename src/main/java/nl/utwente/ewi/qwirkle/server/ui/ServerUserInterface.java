package nl.utwente.ewi.qwirkle.server.ui;

import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.model.PlayerList;
import nl.utwente.ewi.qwirkle.util.Logger;
import nl.utwente.ewi.qwirkle.util.TextAreaOutputStream;

import javax.swing.*;
import java.awt.*;

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
        JList clientListField = new JList(clientList);
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

    public void setStatus(boolean s) {
        if (s) {
            statusText.setForeground(Color.GREEN);
            statusText.setText("Online");
        } else {
            statusText.setForeground(Color.RED);
            statusText.setText("Offline");
        }
    }

    public void setIp(String ip) {
        ipText.setText("IP: " + ip);
    }

    public void setPort(String port) {
        portText.setText("Port: " + port);
    }

    public void updateClients() {
        clientList.clear();
        for (ClientHandler ch : PlayerList.getPlayerList().values()) {
            clientList.addElement(ch.getName() + " (" + ch.getState() + ")");
        }
    }
}
