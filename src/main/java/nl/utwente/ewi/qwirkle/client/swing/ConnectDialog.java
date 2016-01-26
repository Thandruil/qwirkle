package nl.utwente.ewi.qwirkle.client.swing;

import nl.utwente.ewi.qwirkle.client.ClientController;
import nl.utwente.ewi.qwirkle.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectDialog extends JDialog {
    private JButton connectButton;
    private JTextField hostField;
    private JTextField portField;
    private JButton cancelButton;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JPanel inputPane;
    private JPanel contentPane;
    private JPanel buttonPane;

    private ClientController controller;

    public ConnectDialog(ClientController controller, Frame frame) {
        super(frame, "Connect");
        this.controller = controller;
        setupUI();
        setContentPane(contentPane);
        pack();
        setVisible(true);
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private void setupUI() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        inputPane = new JPanel();
        inputPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(inputPane, gbc);
        hostLabel = new JLabel();
        hostLabel.setText("Host");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPane.add(hostLabel, gbc);
        portLabel = new JLabel();
        portLabel.setText("Port");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPane.add(portLabel, gbc);
        hostField = new JTextField();
        hostField.setPreferredSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPane.add(hostField, gbc);
        portField = new JTextField();
        portField.setPreferredSize(new Dimension(100, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPane.add(portField, gbc);
        buttonPane = new JPanel();
        buttonPane.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(buttonPane, gbc);
        connectButton = new JButton();
        connectButton.setAlignmentX(1.0f);
        connectButton.setAlignmentY(0.0f);
        connectButton.setText("Connect");
        connectButton.addActionListener(al -> {
            InetAddress host = null;
            try {
                if (hostField.getText().equals("")) throw new UnknownHostException();
                host = InetAddress.getByName(hostField.getText());
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(null, "Enter a valid host");
            }

            int port = 0;
            try {
                port = Integer.parseInt(portField.getText());
                if (port <= 0 && port > 65535) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                port = 0;
                JOptionPane.showMessageDialog(null, "Enter a valid port");
            }

            if (host != null && port != 0) {
                try {
                    controller.connect(host, port);
                    String name = JOptionPane.showInputDialog("Playername?");
                    controller.identify(name);
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(connectButton, gbc);
        cancelButton = new JButton();
        cancelButton.setAlignmentX(1.0f);
        cancelButton.setAlignmentY(0.0f);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(al -> close());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(cancelButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPane.add(spacer1, gbc);
    }
}
