import java.util.ArrayList;
import java.util.HashMap;

public class Company {
    public ArrayList<int[]> data = new ArrayList<>();
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
        data.add(new int[]{i, (int) (Math.random() * 400)});
    }

}
