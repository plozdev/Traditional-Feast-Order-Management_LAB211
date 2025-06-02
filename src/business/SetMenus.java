package business;

import model.SetMenu;
import repository.SetMenuRepository;
import tools.Workable;

import java.util.*;

public class SetMenus implements Workable<SetMenu> {
    private final String pathFile;
    private boolean isFileAvailable = true;
    private transient final SetMenuRepository repo;
    private Map<String, SetMenu> setMenuMap;
    public SetMenus(String pathFile) {
        super();
        this.pathFile = pathFile;
        this.repo = new SetMenuRepository();
        this.setMenuMap = new HashMap<>();
        readFromFile(); //Tải dữ liệu khi khởi tạo
    }

    /**
     * Checking if menuId is existed in list
     * @param menuId - ID code of dishes in menu
     * @return True if menu is existed in list, otherwise false
     */
    public boolean isValidMenuId(String menuId) { return this.setMenuMap.containsKey(menuId); }

    /**
     * Determines the availability of a file.
     *
     * @return true if the file is available; false otherwise.
     */
    public boolean isAvailableFile() { return this.isFileAvailable; }

    /**
     * Show list of dished on the menu
     */
    public void showMenuList() {
        if (!isFileAvailable) { //Nếu không tìm thấy file để load
            System.out.println("Cannot read data from \"feastMenu.csv\". Please check it.");
            return;
        }
        if (this.setMenuMap.isEmpty()) { //If menu is empty
            System.out.println("No menu items found.");
            return;
        }

        System.out.println("----------------------------------------------------------------");
        System.out.println("List of Set Menus for ordering party:");
        System.out.println("----------------------------------------------------------------");
        List<SetMenu> list = new ArrayList<>(this.setMenuMap.values());
        list.sort(Comparator.comparingLong(SetMenu::getPrice));
        for (SetMenu menu : list) {
            System.out.println(menu);
            System.out.println("----------------------------------------------------------------");
        }
    }

    public SetMenu getMenuById(String menuId) {
        if (menuId == null) return null;
        return this.setMenuMap.get(menuId.toUpperCase());
    }

    // --- Các phương thức Workable ---
    @Override
    public void addNew(SetMenu x) {
        //không hỗ trợ
    }

    @Override
    public void update(SetMenu x) {
        //không hỗ trợ
    }

    @Override
    public SetMenu searchById(String id) {
        return getMenuById(id);
    }

    @Override
    public void showAll() {
        showMenuList();
    }

    @Override
    public boolean isSaved() {
        return true; //always true because cannot edit
    }

    @Override
    public void saveToFile() {
        //not support
    }

    /**
     *
     */
    @Override
    public void readFromFile() {
        this.setMenuMap = repo.loadFromFile(this.pathFile);
        this.isFileAvailable = repo.fileExistsAndReadable(this.pathFile) &&
                            !this.setMenuMap.isEmpty();

        if (!repo.fileExistsAndReadable(this.pathFile)) {
            System.err.println("Warning: Menu data file is not available or readable.");
        }
        else if (!isFileAvailable &&
            repo.fileExistsAndReadable(this.pathFile) &&
            this.setMenuMap.isEmpty()) {
            System.err.println("Warning: Menu data file is empty.");
        }
    }

}
