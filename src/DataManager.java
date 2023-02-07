import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class DataManager {

    private HashMap<String, String> companies = new HashMap<String, String>();

    public void loadCompanies() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("dataFiles/tickerSymbols.txt"));
            String line = in.readLine();
            while((line = in.readLine()) != null) {
                String[] pair = line.split("-//-");
                companies.put(pair[0], pair[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getCompanies() {
        return companies;
    }


}
