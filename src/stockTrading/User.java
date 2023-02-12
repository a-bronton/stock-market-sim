package stockTrading;

import java.io.*;

public class User {
    private double balance;

    public void addToBalance(double amount) {
        balance += amount;
    }

    public void subtractFromBalance(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public void loadData() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("dataFiles/userData.txt"));

            String line = in.readLine(); // READ METADATA
            line = in.readLine(); // READ ACTUAL DATA

            String[] data = line.split(",");
            balance = Double.parseDouble(data[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/userData.txt"));
            out.println("[BALANCE]");
            out.println(balance);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
