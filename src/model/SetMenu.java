package model;

import java.io.Serializable;
public class SetMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    private String menuId, menuName;
    private long price;
    private String ingredients;

    public SetMenu(String menuId, String menuName, long price, String ingredients) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.ingredients = ingredients;
    }

    public SetMenu() {
    }

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


    @Override
    public String toString() {
        return String.format("Code       :%s\nName       :%s\nPrice      :%,d Vnd\nIngredients:\n%s",
                menuId,
                menuName,
                price,
                ingredients);
    }
}
