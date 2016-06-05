package materialapp.survivingwithandroid.com.materialapp.model;

/**
 * Created by dganeshappa on 5/18/2016.
 */
public class MenuItem {
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    int price;
    String name;
    String id;
    boolean available;
    int  no_of_order;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public int getNo_of_order() {
        return no_of_order;
    }
    public void setNo_of_order(int no_of_order) {
        this.no_of_order = no_of_order;
    }
    public MenuItem()
    {
        name = new String();
        id = new String();
        available = true;
        no_of_order = 0;

    }
}
