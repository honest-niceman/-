package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class LowView  extends  JFrame{
    private JPanel panel;
    private JTextField textField1;
    private JLabel answer;
    private JButton CompleteButton, backToTheMain;
    private JButton loaddllButton;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private LogForm logForm;
    private String strBitwise;
    private String path;
    private JFileChooser fileChooser;

    public LowView(LogForm logForm) {
        super("Низкоуровневая функция");
        this.logForm = logForm;
        fileChooser = new JFileChooser();
        createUI();
    }

    public void createUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textField1.setVisible(false);
        CompleteButton.setVisible(false);
        l1.setVisible(false);
        l2.setVisible(false);
        l3.setVisible(false);

        setContentPane(panel);
        setVisible(true);
        setSize(600,220);

        loaddllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog(LowView.this) == JFileChooser.APPROVE_OPTION){
                    if(fileChooser.getSelectedFile().getPath().endsWith(".dll")){
                        LoadDll();
                    }
                    else {
                        JOptionPane.showMessageDialog(LowView.this,
                                "Выберите прилагаемую к СПО .dll","Ошибка",JOptionPane.ERROR_MESSAGE);
                        logForm.addAction("Попытка выбрать не .dll файл");
                    }
                }
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c == '0') || (c == '1') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });

        CompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyReflection();
            }
        });
        backToTheMain.addActionListener(new ActionListener() {
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

    public void LoadDll(){
        path = fileChooser.getSelectedFile().getPath();
        int k = 0;
        try {
            Class LowLevel = Class.forName("TestJNI");
            Constructor native_constructor = LowLevel.getConstructor();
            Method load_method = LowLevel.getMethod("Load",String.class);
            Object natObj = native_constructor.newInstance();
            k = (int) load_method.invoke(natObj,path);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(LowView.this, "Выберите прилагаемую к СПО .dll",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Загружена неверная .dll библиотека");
        }
        if(k == -1){
            JOptionPane.showMessageDialog(LowView.this, "Выберите прилагаемую к СПО .dll",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Ошибка при подключении .dll");
        }else if (k == 1){
            logForm.addAction(".dll подключена");
            textField1.setVisible(true);
            CompleteButton.setVisible(true);
            l1.setVisible(true);
            l2.setVisible(true);
            l3.setVisible(true);
        }
    }

    public void MyReflection(){
        strBitwise = textField1.getText();
        try {
            Class LowLevel = Class.forName("TestJNI");
            Constructor native_constructor = LowLevel.getConstructor();
            Method native_method = LowLevel.getMethod("bitwiseNot",String.class);
            Object natObj = native_constructor.newInstance();
            answer.setText((String) native_method.invoke(natObj,strBitwise));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(LowView.this, "Выберите прилагаемую к СПО .dll",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Загружена неверная .dll библиотека");
        }
    }
}
