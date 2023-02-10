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

    public void addData() {
        i++;
        double randVal = data.get(data.size() - 1)[1] + (Math.random() * 20) - 10;
        if (randVal < 2) {
            randVal = 2;
        }
        data.add(new double[]{i, randVal});
    }

}
