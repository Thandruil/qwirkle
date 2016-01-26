package nl.utwente.ewi.qwirkle.server.ui;

import javax.swing.*;
import java.awt.event.*;

/**
 * Creates a dialog in which the user can choose a port for the server.
 */
public class ChoosePort extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField portField;

    private int port;
    private boolean enter;

    /**
     * Initializes the dialog and opens it.
     */
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

    /**
     * Called when the user pressed OK or the ENTER key.
     */
    private void onOK() {
        this.enter = true;
        try {
            this.port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            this.port = -1;
        }
        dispose();
    }

    /**
     * Called when the user exits the window or presses CANCEL.
     */
    private void onCancel() {
        this.enter = false;
        dispose();
    }

    /**
     * Gives back if the user exited or cancelled the dialog (FALSE) or it the user pressed OK or the ENTER key (TRUE).
     * @return If the user pressed OK or the ENTER key.
     */
    public boolean getEnter() { return this.enter; }

    /**
     * Gives back the port the user chose in this dialog.
     * @return The port the user chose as an Integer.
     */
    public int getPort() { return this.port; }
}
