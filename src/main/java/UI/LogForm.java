package UI;

import javax.swing.*;
import java.util.Date;

/**
 * Класс, реализующий графическое представления формы "Лог сообщений"
 * */
public class LogForm extends JFrame{
    private JTextArea textArea1;
    private JPanel panel1;
    private Date date;

    public void addAction(String str){
        date = new Date();
        textArea1.append(date.toString() + ": " +str+"\n");
    }

    public LogForm() {
        super("Log");
        setSize(300,900);
        setContentPane(panel1);
        setVisible(true);
    }
}
