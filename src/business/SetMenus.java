package business;

import model.SetMenu;
import repository.SetMenuRepository;
import tools.Workable;

import java.util.*;
import tools.FileUtils;

/**
 * Manages a collection of SetMenu Object
 * Implements operations on set of menu data
 * @author mymym
 */
public class SetMenus implements Workable<SetMenu> {
    private final String pathFile;
    private boolean isFileAvailable = true;
    private transient final SetMenuRepository repo;
    private Map<String, SetMenu> setMenuMap;
    
    /**
     * Constructor
     * Initializes the repository, sets up the menu map and load data from file in system.
     * 
     * @param pathFile path file need to read
     */
    public SetMenus(String pathFile) {
        super();
        this.pathFile = pathFile;
        this.repo = new SetMenuRepository();
        this.setMenuMap = new HashMap<>();
        readFromFile(); //Load data
    }

    /**
     * Checking if menuId is existed in list
     * 
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
     * Show list of dished on the menu, sorted by price
     * If the data or file was not found, show the message
     */
    public void showMenuList() {
        if (!isFileAvailable) { //If file was not found
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

    /**
     * Function to check if a menu is existed in system
     * 
     * @param menuId Id of menu
     * @return true if menu is available, otherwise false 
     */
    public SetMenu getMenuById(String menuId) {
        if (menuId == null) return null;
        return this.setMenuMap.get(menuId.toUpperCase());
    }

    // --- Workable methos ---
    @Override
    public void addNew(SetMenu x) {
        //not support
    }

    @Override
    public void update(SetMenu x) {
        //not support
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
    public void saveToFile() {
        //not support
    }

    /**
     * Read set menus from file
     * Prints warning if the file is unavailable
     */
    @Override
    public void readFromFile() {
        this.setMenuMap = repo.loadFromFile(this.pathFile);
        boolean isGoodFile = FileUtils.fileExistsAndReadable(this.pathFile);
        this.isFileAvailable = isGoodFile && !this.setMenuMap.isEmpty();

        if (!isGoodFile) {
            System.err.println("Warning: Menu data file is not available or readable.");
        }
        else if (!isFileAvailable && isGoodFile && this.setMenuMap.isEmpty()) {
            System.err.println("Warning: Menu data file is empty.");
        }
    }

}
