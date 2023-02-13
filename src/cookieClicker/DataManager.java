package cookieClicker;

import javax.swing.*;
import java.io.*;

public class DataManager {
    CookiePanel panel;

    public DataManager(CookiePanel panel) {
        this.panel = panel;
    }

    public void loadData() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("dataFiles/cookieClicker/userData.txt"));

            String line = in.readLine(); // META DATA
            line = in.readLine(); // ACTUAL DATA

            String[] data = line.split(",");
            panel.setCookies(Integer.parseInt(data[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/cookieClicker/userData.txt"));

            out.println("[COOKIES]");
            out.println(panel.getCookies());

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
