import java.util.HashMap;

public class Company {
    public HashMap<String, Integer> data = new HashMap<>();

    private String name;
    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
