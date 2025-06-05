package tools;

/**
 * Defines a generic interface for common CRUD
 * and file operation
 * @param <T>
 */
public interface Workable <T> {
    /**
     * Adds a new object to the collection
     *
     * @param x the object
     */
    void addNew(T x);

    /**
     * Updates an existing object in the collection
     *
     * @param x the object
     */
    void update(T x);

    /**
     * Searches for an object by its unique identifier
     *
     * @param id the id of the object to search for
     * @return the found object, null otherwise
     */
    T searchById(String id);

    /**
     * Display all objects in the collection
     */
    void showAll();

    /**
     * Saves the current state of the collection to a file
     */
    void saveToFile();

    /**
     * Reads data from a file into collection
     */
    void readFromFile();
}
