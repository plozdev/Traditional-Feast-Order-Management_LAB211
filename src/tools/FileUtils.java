package tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static <T>List<T> loadData(String filePath) {
        List <T> list = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File f = new File(filePath);
            if (!f.exists() || !f.canRead()) {
                System.out.println("File not found");
                return list;
            }
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                list.add((T)ois.readObject());
            }
            ois.close();
        } catch (Exception e) {
            System.err.println("Failed to load file: " + filePath);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                System.err.println("Failed to close file");
            }
        }
        return list;
    }

    public static <T> void saveData(String filePath, List<T> list) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            for (T t : list) {
                oos.writeObject(t);
            }
            oos.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Failed to save file");
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                System.err.println("Failed to close file");
            }
        }
    }
}
