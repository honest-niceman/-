package Exeptions;

import UI.ExceptionView;

public class NoSuchNoteException extends Exception {
    public NoSuchNoteException(){}
    public NoSuchNoteException(String message){
        super(message);
    }
}
