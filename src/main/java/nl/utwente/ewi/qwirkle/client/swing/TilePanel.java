package nl.utwente.ewi.qwirkle.client.swing;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.util.Logger;

import javax.swing.*;
import java.awt.*;

public class TilePanel extends JPanel {
    private GridBagConstraints gbc;

    public TilePanel() {
        super();
        setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
    }

    public void update(Board board) {
        this.removeAll();
        int[] boundaries = board.getBoundaries();

        for (int y = Math.max(boundaries[0], 3); y >= Math.min(boundaries[2], -3); y--) {
            for (int x = Math.min(boundaries[3], -3); x <= Math.max(boundaries[1], 3); x++) {
                Tile tile = board.get(new Coordinate(x, y));
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));
                gbc.gridx = x + Math.max(boundaries[3], 3);
                gbc.gridy = y - Math.max(boundaries[0], 3);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                if (tile != null) {
                    button.setText(String.valueOf(tile.hashCode()));
                    button.setEnabled(false);
                }
                this.add(button, gbc);
            }
        }
        this.repaint();
    }
}
