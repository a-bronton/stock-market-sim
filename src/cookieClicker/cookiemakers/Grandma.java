package cookieClicker.cookiemakers;

public class Grandma extends CookieMaker {

    public static int count = 0;
    public Grandma() {
        super("Grandma", 10, 2, "grandma.png");
        count++;
        System.out.println("+1 Grandma");
    }
}
