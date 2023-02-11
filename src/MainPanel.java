import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.attribute.UserPrincipalLookupService;
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
  private JLabel unitsOwnedLabel;
  private JLabel balanceLabel;

  // TODO: Utilities
  DataManager dMan = new DataManager();
  boolean dotsEnabled = true;

  // TODO: USER
  User user = new User(500);
  int unitsSelected = 1;

  public MainPanel() {
    // TODO: Initial Setup
    setPreferredSize(new Dimension(900, 650));
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
    stockGraph.setBounds(100, 25, 560, 425);
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

    balanceLabel = new JLabel("Balance: $" + user.getBalance());
    balanceLabel.setForeground(Color.WHITE);
    balanceLabel.setFont(font1.deriveFont(Font.BOLD, 18));
    balanceLabel.setBounds(100, 470, 200, 40);
    centerPanel.add(balanceLabel);

    JButton buyButton = new JButton("Buy: " + unitsSelected + "x");
    buyButton.setBorder(new MatteBorder(3, 0, 0, 0, bgColor));
    buyButton.setBackground(color2);
    buyButton.setFocusable(false);
    buyButton.setForeground(Color.WHITE);
    buyButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80, 450, 80, 30);
    centerPanel.add(buyButton);

    JButton sellButton = new JButton("Sell: " + unitsSelected + "x");
    sellButton.setBorder(new MatteBorder(3, 0, 0, 0, bgColor));
    sellButton.setBackground(color2);
    sellButton.setFocusable(false);
    sellButton.setForeground(Color.WHITE);
    sellButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80, 480, 80, 30);
    centerPanel.add(sellButton);

    JButton incrementUpButton = new JButton("+");
    incrementUpButton.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    incrementUpButton.setForeground(Color.WHITE);
    incrementUpButton.setBackground(color2);
    incrementUpButton.setFocusable(false);
    incrementUpButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80 - 30, 450, 30, 30);
    centerPanel.add(incrementUpButton);

    JButton incrementDownButton = new JButton("-");
    incrementDownButton.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    incrementDownButton.setForeground(Color.WHITE);
    incrementDownButton.setBackground(color2);
    incrementDownButton.setFocusable(false);
    incrementDownButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80 - 30, 480, 30, 30);
    centerPanel.add(incrementDownButton);

    unitsOwnedLabel = new JLabel("Units Owned: 0");
    unitsOwnedLabel.setForeground(Color.WHITE);
    unitsOwnedLabel.setBounds((int) stockGraph.getBounds().getWidth() - 135, 450, 135, 30);
    unitsOwnedLabel.setFont(font1.deriveFont(Font.PLAIN, 15));
    centerPanel.add(unitsOwnedLabel);

    JTextField selectUnitsField = new JTextField(15);
    selectUnitsField.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80, 510, 80, 20);
    selectUnitsField.setBackground(color2);
    selectUnitsField.setBorder(new MatteBorder(3, 0, 0, 0, bgColor));
    selectUnitsField.setForeground(Color.WHITE);
    selectUnitsField.setCaretColor(Color.WHITE);
    selectUnitsField.setFont(font1.deriveFont(Font.BOLD, 12));
    selectUnitsField.setHorizontalAlignment(JTextField.CENTER);
    centerPanel.add(selectUnitsField);

    setupTradingButtons(buyButton, sellButton, incrementUpButton, incrementDownButton, selectUnitsField);

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
          unitsOwnedLabel.setText("Units Owned: " + dMan.getCompany(selectedCompany).getUnitsOwned());
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
    if (c.getCurrentValue() <= data.get(data.size() - 2)[1]) {
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
    DecimalFormat df = new DecimalFormat("0.00");
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
    String opening = "Opening: $" + df.format(c.getOpening());
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

  public void setupTradingButtons(JButton buyButton, JButton sellButton, JButton incrementUpButton, JButton incrementDownButton, JTextField selectUnitsField) {
    DecimalFormat df = new DecimalFormat("0.00");
    buyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Company company = dMan.getCompany(selectedCompany);
        if (user.getBalance() >= company.getCurrentValue() * unitsSelected) {
          user.subtractFromBalance(company.getCurrentValue());
          balanceLabel.setText("Balance: $" + df.format(user.getBalance()));
          company.addUnitsOwned(unitsSelected);
          unitsOwnedLabel.setText("Units Owned: " + company.getUnitsOwned());
        } else {
          JOptionPane.showMessageDialog(null, "Insufficient Funds");
        }
      }
    });

    sellButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (dMan.getCompany(selectedCompany).getUnitsOwned() >= unitsSelected) {
          user.addToBalance(dMan.getCompany(selectedCompany).getCurrentValue() * unitsSelected);
          balanceLabel.setText("Balance: $" + df.format(user.getBalance()));
          dMan.getCompany(selectedCompany).addUnitsOwned(-unitsSelected);
          unitsOwnedLabel.setText("Units Owned: " + dMan.getCompany(selectedCompany).getUnitsOwned());
        }
      }
    });

    incrementUpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        unitsSelected++;
        buyButton.setText("Buy: " + unitsSelected + "x");
        sellButton.setText("Sell: " + unitsSelected + "x");
      }
    });

    incrementDownButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (unitsSelected > 1) {
          unitsSelected--;
          buyButton.setText("Buy: " + unitsSelected + "x");
          sellButton.setText("Sell: " + unitsSelected + "x");
        }
      }
    });

    selectUnitsField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          unitsSelected = Integer.parseInt(selectUnitsField.getText());
          buyButton.setText("Buy: " + unitsSelected + "x");
          sellButton.setText("Sell: " + unitsSelected + "x");
          selectUnitsField.setText("");
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(null, "Invalid Input");
        }
      }
    });
  }
}
