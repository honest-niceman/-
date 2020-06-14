package Buissines;

import Exeptions.AlreadyExistException;
import Exeptions.NoSuchNoteException;
import MVP.MyFileContract;

import java.io.*;
import java.util.ArrayList;

/**
 *  Это класс - для работы с бинарными файлами.
 *  Класс содержит методы для создания и удаления файла.
 *  Класс инкапсулирует в себе список записей
 *  Класс содержит методы для работы с записями:
 *  добавление, удаление, редактирование.
 *  Каждая запись характеризуется:
 *  Названием файла, размером файла, датой создания файла.
 * */

public class MyFile implements MyFileContract.Presenter {
    private File file = null;
    private String pathName = "Default";
    private DataOutputStream DOS = null;
    private DataInputStream DIS = null;

    MyFileContract.View view;

    /**
     * Список записей содержащихся в файле
     */
    private ArrayList<MyNote> notes = new ArrayList<>();

    public MyFile() {

    }

    /**
     * Сеттер для установки пути к файлу
     * @param pathName полный путь к файлу
     * */
    @Override
    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * Сеттер для списка записей
     * @param notes список записей, которые необходимо присвоить файлу
     * */
    @Override
    public void setNotes(ArrayList<MyNote> notes) {
        this.notes = notes;
    }

    /**
     * Геттер для списка записей
     * @return возвращает список записей текущего файла
     * */
    @Override
    public ArrayList<MyNote> getNotes() {
        return notes;
    }

    /**
     * Конструктор, который используется для взаимодействия
     * частей модели MVP
     * @param view контракт для средства отображения
     * */
    public MyFile(MyFileContract.View view){
        this.view = view;
    }

    /**
     * Конструктор для создания объекта класса.
     * Физически создаётся файла и средства для работы с ним.
     * @param pathName - путь + название файла
     * @throws IOException выбрасывается исключение в случае отсутсвия файла
     */
    public MyFile(String pathName) throws IOException {
        this.pathName = pathName;
        file = new File(pathName);
        DOS = new DataOutputStream(new FileOutputStream(file));
    }

    /**
     * Метод перезаписи всех записей файла
     * @throws IOException выбрасывается исключение в случае отсутсвия файла
     */
    @Override
    public void writeFile() throws IOException {
        file.delete();
        file = new File(pathName);
        DOS = new DataOutputStream(new FileOutputStream(file));
        byte[] bytes;
        DOS.writeInt(notes.size());
        for (int i = 0; i < notes.size(); i++) {
            //write path
            bytes = notes.get(i).getFilePath().getBytes();
            DOS.writeInt(bytes.length);
            DOS.write(bytes);
            //write size
            DOS.writeInt(notes.get(i).getFileSize());
            //write date
            bytes = notes.get(i).getDateOfCreation().getBytes();
            DOS.writeInt(bytes.length);
            DOS.write(bytes);
        }
    }

    /**
     * Метод, реализующий проверку существования записи
     *
     * @param DateOfCreation дата создания файла
     * @param FilePath       путь к файлу
     * @param FileSize       размер файла
     * @return  true если запись существует
     *          false если записи не существует
     */
    @Override
    public boolean isNoteExist(String FilePath, int FileSize, String DateOfCreation) {
        if (notes.size() == 0) return false;
        for (MyNote note : notes) {
            if (note.getFilePath().equals(FilePath) &&
                    note.getDateOfCreation().equals(DateOfCreation) &&
                    note.getFileSize() == FileSize) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод добавления записи в файл
     *
     * @param DateOfCreation дата создания файла
     * @param FilePath       путь к файлу
     * @param FileSize       размер файла
     * @throws IOException выбрасывается в случае отсутсвия файла
     * @throws AlreadyExistException выбрасывается в случае попытки добавить
     *                              запись, уже существующей в данном файле
     */
    @Override
    public void addNote(String FilePath, int FileSize, String DateOfCreation) throws IOException, AlreadyExistException {
        byte[] bytes;
        if (isNoteExist(FilePath, FileSize, DateOfCreation) == false) {
            notes.add(new MyNote(FilePath, FileSize, DateOfCreation));
            writeFile();
        }
        else throw new AlreadyExistException("Запись с такими параметрами уже существует!");
    }

    /**
     * Метод удаления записи из файла
     *
     * @param DateOfCreation дата создания файла
     * @param FilePath       путь к файлу
     * @param FileSize       размер файла
     * @throws IOException выбрасывается исключение в случае отсутсвия файла
     * @throws NoSuchNoteException выбрасывается в случае попытки обращения к
     *                              записи, несуществующей в данном файле
     */
    @Override
    public void deleteNote(String FilePath, int FileSize, String DateOfCreation) throws IOException, NoSuchNoteException {
        if(isNoteExist(FilePath,FileSize,DateOfCreation)){
            int index = -1;
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getFilePath().equals(FilePath) &&
                        notes.get(i).getDateOfCreation().equals(DateOfCreation) &&
                        notes.get(i).getFileSize() == FileSize) {
                    index = i;
                    break;
                }
            }
            notes.remove(index);
            writeFile();
        }
        else throw new NoSuchNoteException("Записи с такими параметрами не существует");
    }

    /**
     * Метод редактирования записи
     *
     * @param oldDateOfCreation старая дата создания редактируемого записи
     * @param oldFilePath       старый путь редактируемого записи
     * @param oldFileSize       старый размер редактируемого записи
     * @param newDateOfCreation новая дата для редактируемого записи
     * @param newFilePath       новый путь редактируемого записи
     * @param newFileSize       новый размер редактируемой записи
     * @throws IOException выбрасывается исключение в случае отсутсвия файла
     * @throws NoSuchNoteException выбрасывается в случае попытки обращения к
     *                              записи, несуществующей в данном файле
     */
    @Override
    public void editNote(String oldFilePath, int oldFileSize, String oldDateOfCreation,
                         String newFilePath, int newFileSize, String newDateOfCreation) throws IOException, NoSuchNoteException, AlreadyExistException {
        if (isNoteExist(oldFilePath, oldFileSize, oldDateOfCreation) == true) {
            if(isNoteExist(newFilePath,newFileSize,newDateOfCreation) == false) {
                int index = -1;
                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getFilePath().equals(oldFilePath) &&
                            notes.get(i).getDateOfCreation().equals(oldDateOfCreation) &&
                            notes.get(i).getFileSize() == oldFileSize) {
                        index = i;
                        break;
                    }
                }
                notes.get(index).setDateOfCreation(newDateOfCreation);
                notes.get(index).setFilePath(newFilePath);
                notes.get(index).setFileSize(newFileSize);
                writeFile();
            }
            else throw new AlreadyExistException("Запись с такими параметрами уже существует");
        } else throw new NoSuchNoteException("Записи с такими параметрами не существует");
    }

    /**
     * Метод удаления файла
     */
    @Override
    public void deleteFile(String pathName) {
        file = new File(pathName);
        if (file.exists()) {
            notes.clear();
            file.delete();
        }
    }

    /**
     * Метод для чтения записей из файла
     * @param pathName - путь к файлу, из которого необходимо прочитать записи
     * @return возвращает список записей, содержащихся в файле
     */
    @Override
    public ArrayList<MyNote> readFile(String pathName) {
        try {
            DIS = new DataInputStream(new FileInputStream(pathName));
        } catch (FileNotFoundException e) {
            return null;
        }
        notes.clear();
        byte[] bytes;
        try {
            int sizeOfArray = DIS.readInt();
            for (int i = 0; i < sizeOfArray; i++) {
                //read path
                int sizeofPath = DIS.readInt();
                if(sizeofPath>10000) return null;
                bytes = new byte[sizeofPath];
                DIS.read(bytes);
                String path = new String(bytes);
                //read size
                int size = DIS.readInt();
                //read date
                int sizeofdate = DIS.readInt();
                if(sizeofdate > 10) return null;
                bytes = new byte[sizeofdate];
                DIS.read(bytes);
                String date = new String(bytes);

                notes.add(new MyNote(path, size, date));
            }
        }catch (Exception e){
            return null;
        }
        return notes;
    }
}