package cookieClicker.cookiemakers;

import javax.swing.*;

public class Cursor extends CookieMaker {

    public static int count = 0;
    public Cursor() {
        super("Cursor", 2, 1, "cursor.png");
        count++;
        System.out.println("+1 Cursor");
    }
}
