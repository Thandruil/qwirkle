package nl.utwente.ewi.qwirkle.client.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyFrame extends JFrame {

    private JCheckBox queue4;
    private JButton queueButton;
    private JCheckBox queue3;
    private JCheckBox queue2;
    private JList playerList;
    private DefaultListModel<String> playerListModel;
    private JLabel listLabel;
    private JTextArea chatBox;
    private JPanel contentPane;
    private JPanel chatPane;
    private JPanel sidePane;
    private JPanel queuePane;
    private JPanel playerPane;
    private JButton disconnectButton;
    private JTextField chatField;
    private JButton chatButton;
    private JPanel inputPane;
    private JPanel controlPane;

    //private ClientController controller;

    /*
    public LobbyFrame(ClientController controller) {
        super("Lobby");
        this.controller = controller;
        setupUI();
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        controller.requestLobby();
    }

    public void updatePlayers(java.util.List<String> players) {
        playerListModel.clear();
        for (String p : players) {
            playerListModel.addElement(p);
        }
    }

    public void disableQueue() {
        queue2.setEnabled(false);
        queue3.setEnabled(false);
        queue4.setEnabled(false);
        queueButton.setEnabled(false);
    }

    public void enableQueue() {
        queue2.setEnabled(true);
        queue3.setEnabled(true);
        queue4.setEnabled(true);
        queueButton.setEnabled(true);
    }

    public void addChat(String channel, String sender, String message) {
        chatBox.append(String.format("[%s] %s: %s\n", channel, sender, message));
    }

    private void setupUI() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        chatPane = new JPanel();
        chatPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(chatPane, gbc);
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        chatPane.add(chatBox, gbc);
        inputPane = new JPanel();
        inputPane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        chatPane.add(inputPane, gbc);
        chatField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPane.add(chatField, gbc);
        chatButton = new JButton();
        chatButton.setText("Send");
        chatButton.addActionListener(al -> {
            if (!chatField.getText().equals("")) {
                String[] chat = chatField.getText().split(" ");
                if (chat.length > 1) {
                    String channel = chat[0];
                    String[] chatMessage = new String[chat.length - 1];
                    System.arraycopy(chat, 1, chatMessage, 0, chat.length - 1);
                    controller.chat(channel, String.join(" ", chatMessage));
                }
            }
            chatField.setText("");
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPane.add(chatButton, gbc);
        sidePane = new JPanel();
        sidePane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(sidePane, gbc);
        queuePane = new JPanel();
        queuePane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        sidePane.add(queuePane, gbc);
        queue4 = new JCheckBox();
        queue4.setText("4 players");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        queuePane.add(queue4, gbc);
        queue3 = new JCheckBox();
        queue3.setText("3 players");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        queuePane.add(queue3, gbc);
        queue2 = new JCheckBox();
        queue2.setName("");
        queue2.setText("2 players");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        queuePane.add(queue2, gbc);
        queueButton = new JButton();
        queueButton.setText("Queue");
        queueButton.addActionListener(al -> {
            java.util.List<Integer> queues = new ArrayList<>();
            if (queue2.isSelected()) queues.add(2);
            if (queue3.isSelected()) queues.add(3);
            if (queue4.isSelected()) queues.add(4);
            controller.queue(queues);
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        queuePane.add(queueButton, gbc);
        playerPane = new JPanel();
        playerPane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        sidePane.add(playerPane, gbc);
        listLabel = new JLabel();
        listLabel.setText("Players");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        playerPane.add(listLabel, gbc);
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        playerPane.add(playerList, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        sidePane.add(panel1, gbc);
        disconnectButton = new JButton();
        disconnectButton.setText("Disconnect");
        disconnectButton.addActionListener(al -> {

        });
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(disconnectButton, gbc);
    }
    */
}
