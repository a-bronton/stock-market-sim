import java.util.ArrayList;
import java.util.HashMap;

public class Company {
    public ArrayList<double[]> data = new ArrayList<>();
    private int i = data.size();

    private String name;
    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private int unitsOwned = 0;

    public void addData() {
//        if (data.size() > 120) {
//            data.remove(0);
//        }
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
        return data.get(0)[1];
    }

    public double getCurrentValue() {
        return data.get(data.size() - 1)[1];
    }

}
