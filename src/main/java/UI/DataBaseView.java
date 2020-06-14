package UI;

import Buissines.MyFile;
import Buissines.MyNote;
import Hibernate.MyFileDB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DataBaseView extends JFrame {
    private JPanel panel;
    private JButton backToTheMainButton,addNoteButton,deleteNoteButton,
            editNoteButton,clearTableButton,fillTableButton;
    private JTable table1;
    private LogForm logForm;
    private JFileChooser fileChooser;
    private DefaultTableModel model;
    private int id;
    private MyFile myFile = new MyFile();

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public DataBaseView(LogForm logForm) {
        super("Data Base");
        this.logForm = logForm;
        fileChooser = new JFileChooser();
        createUI();
        entityManagerFactory = Persistence.createEntityManagerFactory
                ("org.hibernate.tutorial.jpa");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void createUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Object[] columns = {"Path","Size(Kb)","DateOfCreation"};
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.setColumnIdentifiers(columns);
        table1.setModel(model);

        setSize(600,500);
        setContentPane(panel);
        setVisible(true);

        backToTheMainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTableDB();
                entityManagerFactory.close();
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

        addNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog(DataBaseView.this) == JFileChooser.APPROVE_OPTION){
                    addToDB();
                }
            }
        });

        deleteNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.isRowSelected(table1.getSelectedRow())){
                    deleteFromDB();
                } else {
                    JOptionPane.showMessageDialog(DataBaseView.this,
                            "Выберите запись, которую хотите удалить","Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTableDB();
            }
        });

        editNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.isRowSelected(table1.getSelectedRow())) {
                    if (fileChooser.showOpenDialog(DataBaseView.this) == JFileChooser.APPROVE_OPTION) {
                        editNoteDB();
                    }
                } else {
                    JOptionPane.showMessageDialog(DataBaseView.this,
                            "Выберите запись из таблицы, которую хотите отредактировать",
                            "Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fillTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog(DataBaseView.this) == JFileChooser.APPROVE_OPTION){
                    if(model.getRowCount()!= 0) {
                        clearTableDB();
                    }
                    fillTableDB();
                }
            }
        });
    }

    public void addToDB(){
        String path = fileChooser.getSelectedFile().getPath();
        int size = (int)(fileChooser.getSelectedFile().length()/1000);
        try {
            BasicFileAttributes attr = Files.readAttributes(fileChooser.getSelectedFile().toPath(),
                    BasicFileAttributes.class);
            String dateOfCreation = attr.creationTime().toString();
            dateOfCreation = dateOfCreation.substring(0,dateOfCreation.indexOf('T'));

            if(check(path,size,dateOfCreation) == -1) {
                MyFileDB myFileDB = new MyFileDB(path,size,dateOfCreation);

                entityManager.getTransaction().begin();
                entityManager.persist(myFileDB);
                entityManager.getTransaction().commit();

                model.addRow(new Object[]{path,size,dateOfCreation});

                logForm.addAction("Запись добавлена в таблицу базы данных");
            }
            else {
                JOptionPane.showMessageDialog(DataBaseView.this,
                        "Запись с такими параметрами уже существует в таблице",
                        "Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteFromDB() {
        String path = "";
        int size = 0;
        String date = "";

        for (int i = 0; i < 3; i++) {
            if (i == 0) path = model.getValueAt(table1.getSelectedRow(), i).toString();
            else if (i == 1) size = Integer.parseInt(model.getValueAt(table1.getSelectedRow(), i).toString());
            else if (i == 2) date = model.getValueAt(table1.getSelectedRow(), i).toString();
        }

        if (check(path, size, date) != -1) {
            MyFileDB myFileDB = entityManager.find(MyFileDB.class, id);

            if (myFileDB != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(myFileDB);
                entityManager.getTransaction().commit();

                model.removeRow(table1.getSelectedRow());

                logForm.addAction("Запись удалена из таблицы базы данных");
            }
        }
    }

    public void clearTableDB(){
        ArrayList<MyFileDB> allNotes = null;
        allNotes = (ArrayList<MyFileDB>) entityManager.createQuery(
                    "select myFileDB from MyFileDB myFileDB", MyFileDB.class).getResultList();
        if(allNotes != null) {
            if (allNotes.size() != 0) {
                for (int i = 0; i < allNotes.size(); i++) {
                    id = allNotes.get(i).getId();
                    MyFileDB myFileDB = entityManager.find(MyFileDB.class, id);
                    entityManager.getTransaction().begin();
                    entityManager.remove(myFileDB);
                    entityManager.getTransaction().commit();
                }
                clearTable();
            }
        }
    }

    public void fillTableDB() {
        ArrayList<MyNote> myNotes = null;
        String path = fileChooser.getSelectedFile().getPath();
        if (myFile.readFile(path) != null) {
            myNotes = myFile.readFile(path);
            if(myNotes.size()!=0){
                for (int i = 0; i < myNotes.size(); i++) {
                    MyFileDB myFileDB = new MyFileDB(myNotes.get(i).getFilePath(),
                                                    myNotes.get(i).getFileSize(),
                                                    myNotes.get(i).getDateOfCreation());

                    entityManager.getTransaction().begin();
                    entityManager.persist(myFileDB);
                    entityManager.getTransaction().commit();

                    model.addRow(new Object[]{myNotes.get(i).getFilePath(),
                            myNotes.get(i).getFileSize(),
                            myNotes.get(i).getDateOfCreation()});
                }
                logForm.addAction("Файл открыт и все записи добавлены в таблицу бд");
            }
        }
        else {
            JOptionPane.showMessageDialog(DataBaseView.this,
                    "Выберите файл, созданный в данной программе",
                    "Ошибка",JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Попытка открыть файл, созданный не в данной программе");
        }
    }

    public void editNoteDB(){
        String newPath = fileChooser.getSelectedFile().getPath();
        int newSize = (int) (fileChooser.getSelectedFile().length() / 1000);
        String newDateOfCreation = "";

        String path = "";
        String dateOfCreation = "";
        int size = 0;
        try {
            BasicFileAttributes attr = Files.readAttributes(fileChooser.getSelectedFile().toPath(),
                    BasicFileAttributes.class);
            newDateOfCreation = attr.creationTime().toString();
            newDateOfCreation = newDateOfCreation.substring(0, newDateOfCreation.indexOf('T'));

            for (int i = 0; i < 3; i++) {
                if (i == 0) path = model.getValueAt(table1.getSelectedRow(), i).toString();
                if (i == 1) size = Integer.parseInt(model.getValueAt(table1.getSelectedRow(), i).toString());
                if (i == 2) dateOfCreation = model.getValueAt(table1.getSelectedRow(), i).toString();
            }

            if (check(newPath, newSize, newDateOfCreation) == -1) {
                id = check(path, size, dateOfCreation);

                MyFileDB myFileDB = entityManager.find(MyFileDB.class, id);

                myFileDB.setDateOfCreation(newDateOfCreation);
                myFileDB.setSize(newSize);
                myFileDB.setPath(newPath);

                entityManager.getTransaction().begin();
                entityManager.persist(myFileDB);
                entityManager.getTransaction().commit();

                for (int i = 0; i < 3; i++) {
                    if (i == 0) model.setValueAt(newPath, table1.getSelectedRow(), i);
                    if (i == 1) model.setValueAt(newSize, table1.getSelectedRow(), i);
                    if (i == 2) model.setValueAt(newDateOfCreation, table1.getSelectedRow(), i);
                }

                logForm.addAction("Запись изменена в таблице базы данных");

            } else {
                JOptionPane.showMessageDialog(DataBaseView.this,
                        "Запись с такими параметрами уже существует в таблице",
                        "Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int check(String path, int size, String data){
        id = -1;
        ArrayList<MyFileDB> allNotes;
        allNotes = (ArrayList<MyFileDB>) entityManager.createQuery(
                "select myFileDB from MyFileDB myFileDB", MyFileDB.class).getResultList();
        for (int i = 0; i < allNotes.size(); i++) {
            if(allNotes.get(i).getDateOfCreation().equals(data) &&
                allNotes.get(i).getPath().equals(path) &&
                allNotes.get(i).getSize() == size){
                id = allNotes.get(i).getId();
                break;
            }
        }
        return id;
    }

    public void clearTable(){
        for (int i = model.getRowCount()-1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
}
