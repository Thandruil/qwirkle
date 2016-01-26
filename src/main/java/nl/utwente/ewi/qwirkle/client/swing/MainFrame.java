package nl.utwente.ewi.qwirkle.client.swing;

import nl.utwente.ewi.qwirkle.client.ClientController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JButton localGameButton;
    private JButton internetGameButton;
    private JButton quitButton;
    private JPanel contentPane;

    private ClientController controller;

    public MainFrame(ClientController controller) {
        super("Qwirkle");
        this.controller = controller;
        setupUI();
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void setupUI() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        localGameButton = new JButton();
        localGameButton.setOpaque(false);
        localGameButton.setPreferredSize(new Dimension(200, 100));
        localGameButton.setText("Local Game");
        localGameButton.addActionListener(al -> controller.showGameFrame());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(localGameButton, gbc);
        quitButton = new JButton();
        quitButton.setPreferredSize(new Dimension(200, 100));
        quitButton.setText("Quit");
        quitButton.addActionListener(al -> System.exit(0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(quitButton, gbc);
        internetGameButton = new JButton();
        internetGameButton.setPreferredSize(new Dimension(200, 100));
        internetGameButton.setText("Internet Game");
        internetGameButton.addActionListener(al -> controller.showConnectDialog());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(internetGameButton, gbc);
    }
}
