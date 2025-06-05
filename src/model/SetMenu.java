package model;

import java.io.Serializable;

/**
 * Represents a set menu item in a catering or restaurant system.
 * This class is serializable to allow for saving and loading menu data.
 */
public class SetMenu implements Serializable {
    private static final long serialVersionUID = 1L; //Recommend for Serializable class
    private String menuId;      // Unique identifier for the menu
    private String menuName;    // Name of the menu
    private long price;         // Price of the menu
    private String ingredients; // Description of ingredients

    /**
     * Constructors a new Set Menu
     *
     * @param menuId - The unique ID of the menu.
     * @param menuName - The name of the menu.
     * @param price - The price of the menu.
     * @param ingredients - The ingredients of the menu.
     */
    public SetMenu(String menuId, String menuName, long price, String ingredients) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.ingredients = ingredients;
    }

    /**
     * Default constructor
     */
    public SetMenu() {
    }

    //Getters & Setters
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    /**
     * Returns a string representation of the SetMenu object.
     * Displays menu details including ID, name, price, and ingredients.
     *
     * @return A formatted string with menu details.
     */
    @Override
    public String toString() {
        return String.format("Code       :%s\nName       :%s\nPrice      :%,d Vnd\nIngredients:\n%s",
                menuId,
                menuName,
                price,
                ingredients);
    }
}
