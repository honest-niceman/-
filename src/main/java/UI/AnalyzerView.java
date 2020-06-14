package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import MVP.AnalyzerPresenter;

/**
 * Класс, реализующий графическое представление формы анализатора
 * */
public class AnalyzerView extends JFrame {
    private JTextArea textArea1;
    private JButton chechExpressionButton;
    private JButton backToTheMainButton;
    private JPanel MainPanel;
    private JLabel Answer;

    private AnalyzerPresenter analyzerPresenter;
    private LogForm logForm;


    public void setAnalyzerPresenter(AnalyzerPresenter analyzerPresenter) {
        this.analyzerPresenter = analyzerPresenter;
    }

    public AnalyzerView(LogForm logForm) {
        super("Analyzer");
        this.logForm = logForm;
        createUI();
    }

    public void createUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(600,500);
        setContentPane(MainPanel);
        setVisible(true);
        Answer.setText("");
        Answer.setVisible(true);

        chechExpressionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logForm.addAction("Выполнен анализ введенной конструкции");
                analyzerPresenter.check(textArea1.getText());
            }
        });

        backToTheMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MainForm mainForm = new MainForm(logForm);
                        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                        int x = (int) ((dimension.getWidth() - mainForm.getWidth()) / 2);
                        int y = (int) ((dimension.getHeight() - mainForm.getHeight()) / 2);
                        mainForm.setLocation(x, y);
                        logForm.addAction("Открыта главная страница");
                    }
                });
            }
        });
    }

    public void updateAnswerLabel(String answer){
        Answer.setText(answer);
    }
}
