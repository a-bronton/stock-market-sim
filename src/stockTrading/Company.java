package stockTrading;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Company {
    public LinkedList<double[]> data = new LinkedList<>();
    private int i = data.size();
    private double opening;

    private String name;
    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private int unitsOwned = 0;

    public void setOpening() {
        opening = data.get(0)[1];
    }

    public void addData() {
        if (data.size() > 120) {
            data.remove(0);
        }
        i++;

        double randVal = data.get(data.size() - 1)[1] + (Math.random() * 20) - 10;
        if (randVal < 2) {
            randVal = 2;
        }

        int crashRNG = (int) (Math.random() * 1000);
        if (crashRNG == 3) {
            randVal = 0;
        }
        data.add(new double[]{i, randVal});
    }

    public int getUnitsOwned() {
        return unitsOwned;
    }

    public void addUnitsOwned(int units) {
        unitsOwned += units;
    }

    public double getOpening() {
        return opening;
    }

    public double getCurrentValue() {
        return data.get(data.size() - 1)[1];
    }

    public void saveData() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/companyDataFiles/" + name + ".txt"));
            out.println("[MINUTE], [VALUE], [SHARES OWNED = " + unitsOwned + "]");

            for (double[] pair : data) {
                out.println(pair[0] + "," + pair[1]);
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
