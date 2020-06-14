package UI;

import Buissines.MyFile;
import Buissines.MyNote;
import Exeptions.AlreadyExistException;
import Exeptions.NoSuchNoteException;
import MVP.MyFileContract;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;


/**
 * Класс, реализующий графическое представление формы работы с файлами
 * */
public class MyFileView extends JFrame implements MyFileContract.View {
    private JButton deleteFileButton, editNoteButton, deleteNoteButton, addNoteButton,
            createNewFileButton,backToTheMainButton,createFileButton,openFileButton;

    private JTable table1;
    private JPanel bigPanel;
    private JFileChooser fileChooser;

    private MyFileContract.Presenter myFilePresenter;
    private String FilePath, path, dateOfCreation;
    private int size;

    private DefaultTableModel model;
    private LogForm logForm;

    public MyFileView(LogForm logForm) throws IOException {
        super("WorkWithFiles");
        this.logForm = logForm;
        myFilePresenter = new MyFile(this);
        myFilePresenter = new MyFile("Default");
        createUI();
    }

    private void createUI(){
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
        setContentPane(bigPanel);
        setVisible(true);

        deleteFileButton.setVisible(false);
        addNoteButton.setVisible(false);
        editNoteButton.setVisible(false);
        deleteNoteButton.setVisible(false);
        createNewFileButton.setVisible(false);

        fileChooser = new JFileChooser();

        addNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog(MyFileView.this) == JFileChooser.APPROVE_OPTION){
                    addToTable();
                }
            }
        });

        deleteNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table1.isRowSelected(table1.getSelectedRow())){
                    deleteFromTable();
                }
                else {
                    JOptionPane.showMessageDialog(MyFileView.this, "Выберите запись из таблицы, " +
                            "которую хотите отредактировать","Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.isRowSelected(table1.getSelectedRow())) {
                    if (fileChooser.showOpenDialog(MyFileView.this) == JFileChooser.APPROVE_OPTION) {
                        editNoteInTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(MyFileView.this, "Выберите запись из таблицы, " +
                                    "которую хотите отредактировать","Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
                myFilePresenter.deleteFile(FilePath);

                createFileButton.setVisible(true);
                createNewFileButton.setVisible(false);
                deleteFileButton.setVisible(false);
                addNoteButton.setVisible(false);
                editNoteButton.setVisible(false);
                deleteNoteButton.setVisible(false);
                logForm.addAction("Файл удален");
            }
        });

        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
                deleteFileButton.setVisible(false);
                addNoteButton.setVisible(false);
                editNoteButton.setVisible(false);
                deleteNoteButton.setVisible(false);
                createNewFileButton.setVisible(false);
                createFileButton.setVisible(true);
            }
        });

        backToTheMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
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

        createFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(fileChooser.showSaveDialog(MyFileView.this) == JFileChooser.APPROVE_OPTION){
                    clearTable();
                    createFile();
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            }
        });

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog(MyFileView.this) == JFileChooser.APPROVE_OPTION){
                    clearTable();
                    FilePath = fileChooser.getSelectedFile().toString();
                    openFile();
                }
            }
        });
    }

    @Override
    public void createFile(){
        FilePath = fileChooser.getSelectedFile().toString();
        try {
            myFilePresenter = new MyFile(FilePath);
            myFilePresenter.setPathName(FilePath);
            logForm.addAction("Файл создан");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        deleteFileButton.setVisible(true);
        addNoteButton.setVisible(true);
        editNoteButton.setVisible(true);
        deleteNoteButton.setVisible(true);
        createNewFileButton.setVisible(true);
        createFileButton.setVisible(false);
    }

    @Override
    public void openFile() {
        ArrayList<MyNote> myNotes = null;
        if (myFilePresenter.readFile(FilePath) != null) {
            myNotes = myFilePresenter.readFile(FilePath);
            if (myNotes.size() != 0) {
                myFilePresenter.setNotes(myNotes);
                myFilePresenter.setPathName(FilePath);
                if (myNotes.size() != 0) {
                    for (int i = 0; i < myNotes.size(); i++) {
                        path = myFilePresenter.getNotes().get(i).getFilePath();
                        size = myFilePresenter.getNotes().get(i).getFileSize();
                        dateOfCreation = myFilePresenter.getNotes().get(i).getDateOfCreation();
                        model.addRow(new Object[]{path, size, dateOfCreation});
                    }
                    logForm.addAction("Файл открыт");
                }
                deleteFileButton.setVisible(true);
                addNoteButton.setVisible(true);
                editNoteButton.setVisible(true);
                deleteNoteButton.setVisible(true);
                createNewFileButton.setVisible(true);
                createFileButton.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Попытка открыть файл, созданный " +
                        "не в данной программе","Ошибка",JOptionPane.ERROR_MESSAGE);
                logForm.addAction("Попытка открыть файл, созданный не в данной программе");
                deleteFileButton.setVisible(false);
                addNoteButton.setVisible(false);
                editNoteButton.setVisible(false);
                deleteNoteButton.setVisible(false);
                createNewFileButton.setVisible(false);
            }
        }
    }

    @Override
    public void addToTable() {
        path = fileChooser.getSelectedFile().getPath();
        size = (int) (fileChooser.getSelectedFile().length() / 1000);
        try {
            BasicFileAttributes attr = Files.readAttributes(fileChooser.getSelectedFile().toPath(),
                    BasicFileAttributes.class);
            dateOfCreation = attr.creationTime().toString();
            dateOfCreation = dateOfCreation.substring(0, dateOfCreation.indexOf('T'));

            myFilePresenter.addNote(path, size, dateOfCreation);
            model.addRow(new Object[]{path, size, dateOfCreation});
            logForm.addAction("Запись добавлена в файл");

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (AlreadyExistException e) {
            JOptionPane.showMessageDialog(this, "Запись с такими параметрами уже " +
                    "есть в данном файле", "Ошибка", JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Попытка добавить запись с уже существующими параметрами");
        }
    }

    @Override
    public void deleteFromTable(){
        String path = "";
        int size = 0;
        String date = "";

        for (int i = 0; i < 3; i++) {
            if(i == 0) path = model.getValueAt(table1.getSelectedRow(),i).toString();
            else if(i == 1) size = Integer.parseInt(model.getValueAt(table1.getSelectedRow(),i).toString());
            else if(i == 2) date = model.getValueAt(table1.getSelectedRow(),i).toString();
        }
        try {
            myFilePresenter.deleteNote(path,size,date);
            logForm.addAction("Запись удалена");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NoSuchNoteException ex){
            JOptionPane.showMessageDialog(this, "Записи с такими параметрами нет " +
                    "в данном файле", "Ошибка", JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Попытка удалить несуществующую запись");
        }
        model.removeRow(table1.getSelectedRow());
    }

    @Override
    public void editNoteInTable() {
        String newPath = fileChooser.getSelectedFile().getPath();
        int newSize = (int) (fileChooser.getSelectedFile().length() / 1000);
        String newDateOfCreation = "";
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
            myFilePresenter.editNote(path, size, dateOfCreation, newPath, newSize, newDateOfCreation);
            logForm.addAction("Запись отредактирована");
            //updating table
            for (int i = 0; i < 3; i++) {
                if (i == 0) model.setValueAt(newPath, table1.getSelectedRow(), i);
                if (i == 1) model.setValueAt(newSize, table1.getSelectedRow(), i);
                if (i == 2) model.setValueAt(newDateOfCreation, table1.getSelectedRow(), i);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NoSuchNoteException ex) {
            JOptionPane.showMessageDialog(this, "Записи с такими параметрами нет " +
                    "в данном файле", "Ошибка", JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Попытка удалить несуществующую запись");
        } catch (AlreadyExistException e) {
            JOptionPane.showMessageDialog(this, "Запись с такими параметрами уже " +
                    "есть в данном файле", "Ошибка", JOptionPane.ERROR_MESSAGE);
            logForm.addAction("Попытка добавить запись с уже существующими параметрами");
        }
    }

    @Override
    public void clearTable(){
        for (int i = model.getRowCount()-1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
}
