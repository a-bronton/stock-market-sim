package cookieClicker.cookiemakers;

import cookieClicker.CookiePanel;

import javax.swing.*;
import java.util.ArrayList;

public class CookieMaker {

    public static String name;
    public int basePrice, cookieProduction;
    public int price;
    public ImageIcon image;

    public CookieMaker(String name, int basePrice, int cookieProduction, String iconPath) {
        this.name = name;
        this.basePrice = basePrice;
        this.cookieProduction = cookieProduction;
        image = new ImageIcon(getClass().getResource("/cookieClicker/makers/" + iconPath));

        price = basePrice;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getProduction() {
        return cookieProduction;
    }

    public ImageIcon getImage() {
        return image;
    }
}
