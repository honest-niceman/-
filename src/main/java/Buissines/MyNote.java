package Buissines;

/**
 * Это класс - для работы с записями о файле.
 * Класс инкапсулирует в себе:
 * Путь к файлу на компьютере, размер файла в килобайтах, и дату создания файла.
 * Содержит геттеры и сеттеры для полей, названных выше.
 */
public class MyNote {
    private String filePath;
    private int fileSize;
    private String dateOfCreation;

    public String getFilePath() {
        return filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public MyNote(String filePath, int fileSize, String dateOfCreation) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.dateOfCreation = dateOfCreation;
    }
}
