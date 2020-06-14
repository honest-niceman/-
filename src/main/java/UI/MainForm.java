package UI;

import Buissines.Analyzer;
import MVP.AnalyzerPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Класс, реализующий графическое представление главной страницы
 * */
public class MainForm extends JFrame {
    private JPanel mainpanel;
    private JButton workWithFileButton, analyzerButton, dataBaseButton, lowLevelFunctionButton;
    private LogForm logForm;

    public MainForm(LogForm logForm) {
        super("Main Page");
        this.logForm = logForm;
        createUI();
    }

    public void createUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(mainpanel);
        setVisible(true);
        setSize(400,100);

        workWithFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyFileView myFileView = new MyFileView(logForm);
                            logForm.addAction("Выбран режим работы с файлами");
                            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                            int x = (int) ((dimension.getWidth() - myFileView.getWidth()) / 2);
                            int y = (int) ((dimension.getHeight() - myFileView.getHeight()) / 2);
                            myFileView.setLocation(x, y);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                dispose();
            }
        });

        analyzerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logForm.addAction("Выбран режим работы с анализатором");
                SwingUtilities.invokeLater(()->{
                    AnalyzerView analyzerView = new AnalyzerView(logForm);
                    analyzerView.setAnalyzerPresenter(new AnalyzerPresenter(new Analyzer(),analyzerView));
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (int) ((dimension.getWidth() - analyzerView.getWidth()) / 2);
                    int y = (int) ((dimension.getHeight() - analyzerView.getHeight()) / 2);
                    analyzerView.setLocation(x, y);
                });
                dispose();
            }
        });

        lowLevelFunctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logForm.addAction("Выбран режим работы с низкоуровневой функцией");
                SwingUtilities.invokeLater(()->{
                    LowView lowView = new LowView(logForm);
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (int) ((dimension.getWidth() - lowView.getWidth()) / 2);
                    int y = (int) ((dimension.getHeight() - lowView.getHeight()) / 2);
                    lowView.setLocation(x, y);
                });
                dispose();
            }
        });

        dataBaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logForm.addAction("Выбран режим работы с БД");
                SwingUtilities.invokeLater(()->{
                    DataBaseView dataBaseView = new DataBaseView(logForm);
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (int) ((dimension.getWidth() - dataBaseView.getWidth()) / 2);
                    int y = (int) ((dimension.getHeight() - dataBaseView.getHeight()) / 2);
                    dataBaseView.setLocation(x, y);
                });
                dispose();
            }
        });
    }
}
