import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends JPanel {

  private int drawDataDelay = 0;

  // TODO: General Fields
  private String selectedCompany;

  // TODO: Colors
  private Color bgColor = new Color(47, 47, 47);
  private Color color2 = new Color(67, 67, 67);
  private Color hoverColor = new Color(124, 0, 0);

  // TODO: Fonts
  private Font font1 = new Font("DIALOG", Font.PLAIN, 12);

  // TODO: Internal Panels
  private JPanel topBar = new JPanel();
  private JPanel leftPanel = new JPanel();
  private JPanel centerPanel = new JPanel(null);
  // Center Panel
  private JPanel stockGraph = new JPanel();
  private JLabel tickerLabel = new JLabel();
  private JLabel companyLabel = new JLabel();

  // TODO: Utilities
  DataManager dMan = new DataManager();

  public MainPanel() {
    // TODO: Initial Setup
    setPreferredSize(new Dimension(900, 600));
    setBackground(bgColor);
    setLayout(new BorderLayout());
    dMan.loadCompanies();
    dMan.loadCompanyData();

    for (Company c : dMan.getCompanies()) {
      if (c != null) {
        System.out.println(c.getName() + " {");
        for (Map.Entry<String, Integer> pair : c.data.entrySet()) {
          System.out.println("   " + pair.getKey() + ": " + pair.getValue());
        }
        System.out.println("}\n");
      }
    }

    for (Map.Entry<String, String> company : dMan.getTickers().entrySet()) {
      System.out.println(company.getKey() + ": " + company.getValue());
    }

    // TODO: Further Setup
    setupPanels();
  }

  private void setupPanels() {
    // TODO: Top Bar
    topBar.setBackground(color2);
    topBar.setPreferredSize(new Dimension(0, 50));

    JLabel title = new JLabel("Stocks");
    title.setFont(font1.deriveFont(Font.BOLD, 32));
    title.setForeground(Color.WHITE);
    topBar.add(title);

    add(topBar, BorderLayout.NORTH);

    // TODO: Left Panel
    leftPanel.setPreferredSize(new Dimension(120, 0));
    leftPanel.setBackground(color2);
    leftPanel.setBorder(new MatteBorder(0, 0, 0, 4, Color.GRAY));

    for (Map.Entry<String, String> company : dMan.getTickers().entrySet()) {
      leftPanel.add(new TickerButton(company.getKey()));
    }

    add(leftPanel, BorderLayout.WEST);

    // TODO: CENTER PANEL
    centerPanel.setBackground(bgColor);

    tickerLabel.setBounds(110, 30, 100, 25);
    tickerLabel.setFont(font1.deriveFont(Font.BOLD, 20));
    tickerLabel.setForeground(Color.WHITE);
    centerPanel.add(tickerLabel);

    companyLabel.setBounds(110, 55, 400, 25);
    companyLabel.setFont(font1.deriveFont(Font.ITALIC, 15));
    companyLabel.setForeground(Color.WHITE);
    centerPanel.add(companyLabel);

    // JPanel topBar = new JPanel();
    // topBar.setBounds(100, 25, 500, 55);
    // topBar.setBackground(bgColor);
    // centerPanel.add(topBar);

    stockGraph.setBackground(color2);
    stockGraph.setBounds(100, 25, 500, 425);
    centerPanel.add(stockGraph);

    add(centerPanel, BorderLayout.CENTER);
  }

  public class TickerButton extends JButton {
    public TickerButton(String text) {
      setText(text);
      setFont(font1.deriveFont(Font.BOLD, 20));
      setForeground(Color.WHITE);
      setBackground(color2);
      setFocusable(false);
      setBorderPainted(false);
      setSize(100, 45);

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          setCursor(new Cursor(Cursor.HAND_CURSOR));
          ((TickerButton) e.getSource()).setBackground(hoverColor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          if (!((TickerButton) e.getSource()).getText().equals(selectedCompany)) {
            ((TickerButton) e.getSource()).setBackground(color2);
          }
        }
      });

      addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

          Graphics2D g2 = (Graphics2D) stockGraph.getGraphics();
          stockGraph.paint(g2);
          g2.setColor(Color.GREEN);

          selectedCompany = ((TickerButton) e.getSource()).getText();
          tickerLabel.setText(selectedCompany);
          companyLabel.setText(dMan.getTickers().get(selectedCompany));
          for (Component c : leftPanel.getComponents()) {
            c.setBackground(color2);
          }
          ((TickerButton) e.getSource()).setBackground(hoverColor);

          // TODO: DISPLAY
          boolean companyFound = false;
          for (int i = 0; i < dMan.getCompanies().length; i++) {
            if (dMan.getCompanies()[i] != null) {
              if (dMan.getCompanies()[i].getName().equals(selectedCompany)) {
                displayData(dMan.getCompanies()[i], g2);
                companyFound = true;
              }
            }
          }
          if (!companyFound) {
            System.out.println("Couldn't Find Company");
          }
        }
      });
    }
  }

  public void displayData(Company c, Graphics g2) {
    System.out.println("Displaying data for: " + c.getName());

    HashMap<String, Integer> data = c.data;
    int increment = (int) (stockGraph.getBounds().getWidth() / data.size());
    System.out.println("Width: " + stockGraph.getBounds().getWidth());
    System.out.println("Increment: " + increment);

    int[][] pts = new int[data.size()][data.size()];

    int x = 0;
    int i = 0;
    for (Map.Entry<String, Integer> pair : data.entrySet()) {
      g2.fillOval(x - 5, (int) stockGraph.getBounds().getHeight() - pair.getValue() - 5, 10, 10);
      pts[i][0] = x;
      pts[i][1] = (int) stockGraph.getBounds().getHeight() - pair.getValue();
      x += increment;
      i++;
    }

    for (int j = 0; j < pts.length - 1; j++) {
      g2.drawLine(pts[j][0], pts[j][1], pts[j + 1][0], pts[j + 1][1]);
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    drawDataDelay++;

    Graphics2D g2 = (Graphics2D) stockGraph.getGraphics();
    if (selectedCompany != null && drawDataDelay > 1000) {
      dMan.getCompany(selectedCompany).addData();
      g2.setColor(Color.GREEN);
      //displayData(dMan.getCompany(selectedCompany), g2);
      drawDataDelay = 0;
    }

    repaint();
  }
}
