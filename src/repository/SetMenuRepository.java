package repository;

import model.SetMenu;
import tools.Acceptable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import tools.FileUtils;

/**
 * 
 * @author mymym
 */
public class SetMenuRepository {
    /**
     * Reads data from csv file path 
     * Creates a File with file path then check if file is available
     * Uses FileReader and BufferedReader to read each line in file
     * Convert line to object then put it in A Map
     * If can't read file of file is unavailable then show error message and return list of empty
     * 
     * @param filePath file path
     * @return A Map contains list of set menus
     */
    public Map<String, SetMenu> loadFromFile(String filePath) {
        Map<String, SetMenu> menuMap = new HashMap<>();
        File file = new File(filePath);
        if (!FileUtils.fileExistsAndReadable(filePath)) {
            return menuMap; //return empty if file can't read
        }

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            br.readLine(); // Skip CSV header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                SetMenu set = dataToObject(line);
                if (set != null && !menuMap.containsKey(set.getMenuId())) { //Checks if file is duplicated or not
                    menuMap.put(set.getMenuId(), set);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            return null;
        } catch (IOException e) {
            System.err.println("Error reading file");
            return null;
        } catch (Exception e) {
            System.err.println("Error parsing file");
            return null;
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException e) {
                System.err.println("Error closing file");
            }
        }
        return menuMap;
    }

    /**
     * 
     * Split each string into String array and parse it in Set Menu object
     * If a line does not contain enough 4 data then return null
     * Check if price data in csv line is valid
     * Split ingredients into many line for better UI 
     * 
     * @param text Each line is CSV
     * @return SetMenu Object
     */
    private SetMenu dataToObject(String text) {
        String[] parts = text.split(",");
        if (parts.length != 4) return null; //Make sure it has four datas
        if (!Acceptable.isValid(parts[2],Acceptable.POSITIVE_DOUBLE_VALID))
            return null;

        String[] ingredients = parts[3].split("[#\"]");
        StringBuilder finalIngredients = new StringBuilder();
        for (String p : ingredients) {
            finalIngredients.append(p).append("\n");
        }

        return new SetMenu(parts[0],parts[1],Long.parseLong(parts[2]),finalIngredients.toString().trim());
    }

    
}