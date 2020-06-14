package MVP;

import Buissines.MyNote;
import Exeptions.AlreadyExistException;
import Exeptions.NoSuchNoteException;

import java.io.IOException;
import java.util.ArrayList;

public interface MyFileContract {

    interface View{
        void createFile();
        void addToTable();
        void deleteFromTable();
        void editNoteInTable();
        void openFile();
        void clearTable();
    }

    interface Presenter{
        void addNote(String FilePath, int FileSize, String DateOfCreation) throws IOException, AlreadyExistException;
        void deleteNote(String FilePath, int FileSize, String DateOfCreation) throws IOException, NoSuchNoteException;
        void editNote(String oldFilePath, int oldFileSize, String oldDateOfCreation,
                      String newFilePath, int newFileSize, String newDateOfCreation) throws IOException, NoSuchNoteException, AlreadyExistException;
        ArrayList<MyNote> readFile(String pathName);
        void writeFile() throws IOException;
        void deleteFile(String pathname);
        ArrayList<MyNote> getNotes();
        void setNotes(ArrayList<MyNote> notes);
        void setPathName(String pathName);
        boolean isNoteExist(String FilePath, int FileSize, String DateOfCreation) throws AlreadyExistException;
    }
}
