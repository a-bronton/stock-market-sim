import java.util.HashMap;

public class Company {
    public HashMap<String, Integer> data = new HashMap<>();
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
    data.put("" + i, (int) (Math.random() * 100));
  }
}
