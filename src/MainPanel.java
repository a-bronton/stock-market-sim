import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends JPanel {

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
  boolean dotsEnabled = true;

  public MainPanel() {
    // TODO: Initial Setup
    setPreferredSize(new Dimension(900, 600));
    setBackground(bgColor);
    setLayout(new BorderLayout());
    dMan.loadCompanies();
    dMan.loadCompanyData();

    new UpdateThread().start();

    for (Company c : dMan.getCompanies()) {
      if (c != null) {
        System.out.println(c.getName() + " {");
        for (double[] pair : c.data) {
          System.out.println("   " + pair[0] + ": " + pair[1]);
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
    topBar.setLayout(null);
    topBar.setBackground(color2);
    topBar.setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), 50));

    JLabel title = new JLabel("Stocks");
    title.setFont(font1.deriveFont(Font.BOLD, 32));
    title.setForeground(Color.WHITE);
    title.setBounds((int) (topBar.getPreferredSize().getWidth() / 2) - 50, 4, 200, 40);
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

    stockGraph.setBackground(color2);
    stockGraph.setBounds(100, 25, 500, 425);
    stockGraph.setDoubleBuffered(true);
    centerPanel.add(stockGraph);

    JLabel dotsToggleLabel = new JLabel("Dots");
    dotsToggleLabel.setBounds(100, 450, 60, 30);
    dotsToggleLabel.setForeground(Color.WHITE);
    centerPanel.add(dotsToggleLabel);

    JButton toggleDotsButton = new JButton(new ImageIcon(getClass().getResource("/switch_on.png")));
    toggleDotsButton.setBounds(130,450, 30,30);
    toggleDotsButton.setBorderPainted(false);
    toggleDotsButton.setContentAreaFilled(false);
    toggleDotsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dotsEnabled = !dotsEnabled;
        if (selectedCompany != null) {
          displayData(dMan.getCompany(selectedCompany), (Graphics2D) stockGraph.getGraphics());
        }
        if (dotsEnabled) {
          toggleDotsButton.setIcon(new ImageIcon(getClass().getResource("/switch_on.png")));
        } else {
          toggleDotsButton.setIcon(new ImageIcon(getClass().getResource("/switch_off.png")));
        }
      }
    });
    toggleDotsButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    });
    centerPanel.add(toggleDotsButton);

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

          selectedCompany = ((TickerButton) e.getSource()).getText();
          tickerLabel.setText(selectedCompany);
          companyLabel.setText(dMan.getTickers().get(selectedCompany));
          for (Component c : leftPanel.getComponents()) {
            c.setBackground(color2);
          }

          ((TickerButton) e.getSource()).setBackground(hoverColor);

          // TODO: DISPLAY
          displayData(dMan.getCompany(selectedCompany), (Graphics2D) stockGraph.getGraphics());
        }
      });

    }
  }

  public void displayData(Company c, Graphics2D g2) {
//    System.out.println("Displaying data for: " + c.getName());
    stockGraph.paint(g2);
    g2.setColor(Color.GREEN);
    g2.setStroke(new BasicStroke(3));
    g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

    ArrayList<double[]> data = c.data;
    if (data.get(data.size() - 1)[1] <= data.get(data.size() - 2)[1]) {
      g2.setColor(Color.RED);
    } else {
      g2.setColor(Color.GREEN);
    }

    double increment = stockGraph.getBounds().getWidth() / data.size();
//    System.out.println("Width: " + stockGraph.getBounds().getWidth());
//    System.out.println("Increment: " + increment);

    int[][] pts = new int[data.size()][data.size()];

    int x = 0;
    int i = 0;
    for (double[] pair : data) {
      if (dotsEnabled) {
        g2.fillOval(x - 3, (int) stockGraph.getBounds().getHeight() - (int) pair[1] - 3, 6, 6);
      }
      pts[i][0] = x;
      pts[i][1] = (int) stockGraph.getBounds().getHeight() - (int) pair[1];
      x += increment;
      i++;
    }

    for (int j = 0; j < pts.length - 1; j++) {
      g2.drawLine(pts[j][0], pts[j][1], pts[j + 1][0], pts[j + 1][1]);
    }

    // TODO: TEXTS
    DecimalFormat df = new DecimalFormat("0.##");
    g2.setColor(Color.WHITE);
    // TICKER SYMBOL
    g2.setFont(font1.deriveFont(Font.BOLD, 22));
    g2.drawString(selectedCompany, 5, 20);
    // COMPANY NAME
    g2.setFont(font1.deriveFont(Font.PLAIN, 18));
    g2.drawString(dMan.getTickers().get(selectedCompany), 5, 40);
    // VALUE
    g2.drawString("$" + df.format(c.data.get(c.data.size() - 1)[1]), 5, 65);
    // $ UP/DOWN SINCE OPEN
    double updown = (c.data.get(c.data.size() - 1)[1] / c.data.get(0)[1]) * 100;
    if (c.data.get(c.data.size() - 1)[1] < c.data.get(0)[1]) {
      g2.setColor(Color.RED);
      try {
        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("down.png")), 0, 68, null);
      } catch (Exception e) {
        e.printStackTrace();
      }
      g2.drawString("-" + df.format(updown) + "%", 30, 90);
    } else {
      g2.setColor(Color.GREEN);
      try {
        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("up.png")), 0, 68, null);
      } catch (Exception e) {
        e.printStackTrace();
      }
      g2.drawString(df.format(updown) + "%", 30, 90);
    }
    // OPENING
    g2.setColor(Color.WHITE);
    String opening = "Opening: $" + df.format(c.data.get(0)[1]);
    int textWidth = (int) g2.getFontMetrics().getStringBounds(opening, g2).getWidth();
    g2.drawString(opening, (int) stockGraph.getBounds().getWidth() - textWidth - 10, 20);
  }

  public class UpdateThread extends Thread {
    @Override
    public void run() {
      while (true) {
        Graphics2D g2 = (Graphics2D) stockGraph.getGraphics();
        if (selectedCompany != null) {
          stockGraph.paint(g2);
          dMan.getCompany(selectedCompany).addData();
          displayData(dMan.getCompany(selectedCompany), g2);
        }
//        System.out.println("Doing it");
        try {
          sleep(1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
