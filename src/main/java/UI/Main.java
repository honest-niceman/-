package UI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *Класс мэйн, запускающий основную форму приложения
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LogForm logForm = new LogForm();
                logForm.addAction("Программа запущена");
                MainForm mainForm = new MainForm(logForm);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - mainForm.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - mainForm.getHeight()) / 2);
                mainForm.setLocation(x, y);
            }
        });
    }
}
