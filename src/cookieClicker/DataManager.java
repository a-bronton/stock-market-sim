package cookieClicker;

import cookieClicker.cookiemakers.CookieMaker;
import cookieClicker.cookiemakers.Cursor;
import cookieClicker.cookiemakers.Grandma;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class DataManager {
    CookiePanel panel;

    public DataManager(CookiePanel panel) {
        this.panel = panel;
    }

    public void loadData() {
        try {
            // TODO: General Data
            BufferedReader in = new BufferedReader(new FileReader("dataFiles/cookieClicker/userData.txt"));

            String line = in.readLine(); // META DATA
            line = in.readLine(); // ACTUAL DATA

            String[] data = line.split(",");
            panel.setCookies(Integer.parseInt(data[0]));

            // TODO: Makers
            in = new BufferedReader(new FileReader("dataFiles/cookieClicker/makerData.txt"));
            line = in.readLine();
            while ((line = in.readLine()) != null) {
                String[] pair = line.split(",");
                panel.addMakers(pair[0], Integer.parseInt(pair[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        try {
            // TODO: General Data
            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/cookieClicker/userData.txt"));

            out.println("[COOKIES]");
            out.println(panel.getCookies());

            out.flush();
            out.close();

            // TODO: Makers
            out = new PrintWriter(new FileWriter("dataFiles/cookieClicker/makerData.txt"));
            out.println("[MAKER TYPE], [AMOUNT]");

            out.println("Cursor," + Cursor.count);
            out.println("Grandma," + Grandma.count);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
