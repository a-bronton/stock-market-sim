package cookieClicker.cookiemakers;

public class CookieMaker {

    public String name;
    public int basePrice, cookieProduction;
    public int price;

    public CookieMaker(String name, int basePrice, int cookieProduction) {
        this.name = name;
        this.basePrice = basePrice;
        this.cookieProduction = cookieProduction;

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
}
