package tools;

public interface Workable <T> {
    void addNew(T x);
    void update(T x);
    T searchById(String id);
    void showAll();
    boolean isSaved();
    void saveToFile();
    void readFromFile();
}
