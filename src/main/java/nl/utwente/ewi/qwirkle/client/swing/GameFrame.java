package nl.utwente.ewi.qwirkle.client.swing;

import nl.utwente.ewi.qwirkle.client.ClientController;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    private JToggleButton[] tiles;
    private JPanel contentPane;
    private JPanel controlPane;
    private JScrollPane tileScrollPane;
    private TilePanel tilePane;

    private JButton tradeButton;
    private JButton moveButton;

    private ClientController controller;

    public GameFrame(ClientController controller) {
        super("Twerkle");
        this.controller = controller;
        setupUI();
        setContentPane(contentPane);
        pack();
        setVisible(true);
    }

    public void updateHand(java.util.List<Tile> hand) {
        for (JToggleButton b : tiles) {
            b.setText("");
            b.setEnabled(false);
        }

        for (int i = 0; i < hand.size(); i++) {
            Tile tile = hand.get(i);
            tiles[i].setText(tile.getColor().name().substring(0, 1) + tile.getShape().name().substring(0, 1).toLowerCase());
            tiles[i].setEnabled(true);
        }
    }

    public void updateBoard(Board board) {
        tilePane.update(board);
        this.repaint();
    }

    private void setupUI() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        controlPane = new JPanel();
        controlPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(controlPane, gbc);
        tiles = new JToggleButton[6];
        for (int i = 0; i < 6; i++) {
            JToggleButton tile = new JToggleButton();
            tile.setPreferredSize(new Dimension(50, 50));
            tile.setMargin(new Insets(1, 1, 1, 1));
            tile.setMinimumSize(new Dimension(50, 50));
            tile.setEnabled(false);
            gbc.gridx = i;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            controlPane.add(tile, gbc);
            tiles[i] = tile;
        }
        final JPanel spacer = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPane.add(spacer, gbc);
        moveButton = new JButton("Move");
        moveButton.setPreferredSize(new Dimension(75, 50));
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPane.add(moveButton, gbc);
        tradeButton = new JButton("Trade");
        tradeButton.setPreferredSize(new Dimension(75, 50));
        tradeButton.addActionListener(al -> {
            java.util.List<Integer> tileList = new ArrayList<>();
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i].isSelected()) {
                    tileList.add(i);
                }
            }
            controller.moveTrade(tileList);
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPane.add(tradeButton, gbc);
        tileScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(tileScrollPane, gbc);
        tilePane = new TilePanel();
        tileScrollPane.setViewportView(tilePane);
    }
}
