package restaurantrush;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<MenuItem> items; 

    public Menu() {
        items = new ArrayList<>();
    }

    public Menu(List<MenuItem> items) {
        this.items = new ArrayList<>(items);
    }
    
    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public MenuItem find(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
