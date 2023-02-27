package stockTrading;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
                companies[i].addUnitsOwned(Integer.parseInt(line.substring(35, line.length() - 1)));
                while ((line = in.readLine()) != null) {
                    String[] pair = line.split(",");
                    companies[i].data.add(new double[]{Double.parseDouble(pair[0]), Double.parseDouble(pair[1])});
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveTickers() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/tickerSymbols.txt"));
            out.println("[TICKER], [COMPANY NAME]");

            for (Map.Entry<String, String> company : tickers.entrySet()) {
                out.println(company.getKey() + "-//-" + company.getValue());
            }

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
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

  public void addCompanyAndTicker(String name, String ticker) {
        tickers.put(ticker, name);

        try {
            File f = new File("dataFiles/companyDataFiles/" + ticker + ".txt");
            f.createNewFile();

            // INITIAL DATA
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.println("[MINUTE], [VALUE], [SHARES OWNED = 0]");
            out.println("0,0");

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
  }

}
