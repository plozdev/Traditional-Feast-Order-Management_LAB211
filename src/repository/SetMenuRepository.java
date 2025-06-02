package repository;

import model.SetMenu;
import tools.Acceptable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SetMenuRepository {
    public Map<String, SetMenu> loadFromFile(String filePath) {
        Map<String, SetMenu> menuMap = new HashMap<>();
        File file = new File(filePath);
        if (!fileExistsAndReadable(filePath)) {
            return menuMap; // Trả về map rỗng nếu file không đọc được
        }

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            br.readLine(); // Bỏ qua dòng header của CSV
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                SetMenu set = dataToObject(line);
                if (set != null && !menuMap.containsKey(set.getMenuId())) { // Kiểm tra trùng lặp ID trong file
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

    private SetMenu dataToObject(String text) {
        String[] parts = text.split(",");
        if (parts.length != 4) return null; //Đảm bảo đủ 4 data
        if (!Acceptable.isValid(parts[2],Acceptable.POSITIVE_DOUBLE_VALID))
            return null;

        String[] ingredients = parts[3].split("[#\"]");
        StringBuilder finalIngredients = new StringBuilder();
        for (String p : ingredients) {
            finalIngredients.append(p).append("\n");
        }

        return new SetMenu(parts[0],parts[1],Long.parseLong(parts[2]),finalIngredients.toString().trim());
    }

    /**
     * Hàm kiểm tra path File có hoạt động được không
     * @param filePath
     * @return true - file tồn tại và đọc được, false - ngược lại
     */
    public boolean fileExistsAndReadable(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }
}