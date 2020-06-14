package UI;

import javax.swing.*;

public class ExceptionView extends JFrame {
    private JPanel panel1;
    private JLabel message;

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public ExceptionView() {
        setContentPane(panel1);
        setVisible(true);
        setSize(500, 100);
        message.setText("");
    }
}
