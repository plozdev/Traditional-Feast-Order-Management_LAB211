package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations, specifically for loading and saving lists of serializable objects.
 */
public class FileUtils {
    
    /**
     * Loads a list of objects from a specified file.
     *
     * @param filePath - The path to the file to load data from.
     * @param <T> - The type of objects in the list.
     * @return A List containing the objects read from the file. Returns an empty list if the file is not found or an error occurs.
     */
    public static <T>List<T> loadData(String filePath) {
        List <T> list = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File f = new File(filePath);
            if (!FileUtils.fileExistsAndReadable(filePath)) { //Checks if file is ready or not
                System.out.println("File not found or cannot be read: " + filePath);
                return list; // Return empty list if file doesn't exist or isn't readable
            }
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            while (fis.available() > 0) { // Check if there's more data to read
                list.add((T) ois.readObject()); // Read object and cast
            }
        } catch (IOException | ClassNotFoundException e) { // Catch relevant exceptions
            System.err.println("Failed to load data from file: " + filePath + ". Error: " + e.getMessage());
        } finally {
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                System.err.println("Failed to close file streams for: " + filePath + ". Error: " + e.getMessage());
            }
        }
        return list;
    }

    /**
     * Saves a list of objects to a specified file using ObjectOutputStream.
     *
     * @param filePath The path to the file where data will be saved.
     * @param list     The list of objects to save.
     * @param <T>      The type of objects in the list.
     */
    public static <T> void saveData(String filePath, List<T> list) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            for (T t : list) {
                oos.writeObject(t); // Write each object to the stream
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found for saving: " + filePath + ". Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed to save data to file: " + filePath + ". Error: " + e.getMessage());
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                System.err.println("Failed to close file streams for: " + filePath + ". Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Function check if file is exists or readable
     * @param filePath
     * @return true if file is exists and readable, false otherwise
     */
    public static boolean fileExistsAndReadable(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }
}