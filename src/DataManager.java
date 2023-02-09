import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.spec.ECField;
import java.util.HashMap;

public class DataManager {

    private HashMap<String, String> tickers = new HashMap<String, String>();
    private Company[] companies = new Company[100];

    public void loadCompanies() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("dataFiles/tickerSymbols.txt"));
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                String[] pair = line.split("-//-");
                tickers.put(pair[0], pair[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCompanyData() {
        File directory = new File("dataFiles/companyDataFiles");
        int i = 0;
        for (File f : directory.listFiles()) {
            companies[i] = new Company(f.toString().substring(27, f.toString().length() - 4));
            try {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String line = in.readLine();
                while ((line = in.readLine()) != null) {
                    String[] pair = line.split(",");
                    companies[i].data.add(new int[]{Integer.parseInt(pair[0]), Integer.parseInt(pair[1])});
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getTickers() {
        return tickers;
    }

    public Company[] getCompanies() {
        return companies;
    }

  public Company getCompany(String name) {
    for (Company c : companies) {
      if (c != null && c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

}
