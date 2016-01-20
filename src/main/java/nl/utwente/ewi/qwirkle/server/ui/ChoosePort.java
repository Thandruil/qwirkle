package nl.utwente.ewi.qwirkle.server.ui;

import javax.swing.*;
import java.awt.event.*;

public class ChoosePort extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField portField;

    private int port;
    private boolean enter;

    public ChoosePort() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    private void onOK() {
        this.enter = true;
        try {
            this.port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            this.port = -1;
        }
        dispose();
    }

    private void onCancel() {
        this.enter = false;
        dispose();
    }

    public boolean getEnter() { return this.enter; }

    public int getPort() { return this.port; }
}
