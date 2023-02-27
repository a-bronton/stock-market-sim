package stockTrading;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Map;

public class MainPanel extends JPanel {

  // TODO: FRAME
  JFrame window;

  // TODO: General Fields
  private String selectedCompany;
  private int updateTime = 3; // (SECONDS)

  // TODO: Colors
  private Color bgColor = new Color(47, 47, 47);
  private Color color2 = new Color(67, 67, 67);
  private Color hoverColor = new Color(124, 0, 0);

  // TODO: Fonts
  private Font font1 = new Font("DIALOG", Font.PLAIN, 12);

  // TODO: Internal Panels
  private JPanel topBar = new JPanel();
  private JPanel leftPanel = new JPanel(new GridBagLayout());
  private JPanel centerPanel = new JPanel(null);
  private JPanel bottomBar = new JPanel(null);
  private JScrollPane leftPanelScrollPane;
  // Center Panel
  private JPanel stockGraph = new JPanel();
  private JLabel tickerLabel = new JLabel();
  private JLabel companyLabel = new JLabel();
  private JLabel unitsOwnedLabel;
  private JLabel balanceLabel;
  // Bottom Bar
  JButton addNewCompanyButton;

  // TODO: Utilities
  DataManager dMan = new DataManager();
  boolean dotsEnabled = false;

  // TODO: USER
  User user = new User();
  int unitsSelected = 1;

  public MainPanel(JFrame window) {
    this.window = window;

    // TODO: Initial Setup
    // LOAD USER DATA
    user.loadData();

    // SETUP PANEL
    setPreferredSize(new Dimension(900, 750));
    setBackground(bgColor);
    setLayout(new BorderLayout());
    dMan.loadCompanies();
    dMan.loadCompanyData();
    new UpdateThread().start();

    // TODO: PRINT COMPANY DATA
    for (Company c : dMan.getCompanies()) {
      if (c != null) {
        System.out.println(c.getName() + " {");
        for (double[] pair : c.data) {
          System.out.println("   " + pair[0] + ": " + pair[1]);
        }
        System.out.println("}\n");
        c.setOpening();
      }
    }

    // TODO: PRINT COMPANY TICKERS
    for (Map.Entry<String, String> company : dMan.getTickers().entrySet()) {
      System.out.println(company.getKey() + ": " + company.getValue());
    }

    // TODO: Further Setup
    setupPanels();

    // TODO: UPON CLOSING
    window.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        for (Company c : dMan.getCompanies()) {
          if (c != null) {
            c.saveData();
          }
        }
        user.saveData();
        dMan.saveTickers();
      }
    });
  }

  private void setupPanels() {
    // TODO: Top Bar
    topBar.setLayout(null);
    topBar.setBackground(color2);
    topBar.setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), 50));
    add(topBar, BorderLayout.NORTH);

    JLabel title = new JLabel("STOCKS");
    title.setFont(font1.deriveFont(Font.BOLD, 32));
    title.setForeground(Color.WHITE);
    title.setBounds((int) (topBar.getPreferredSize().getWidth() / 2) - 50, 4, 200, 40);
    topBar.add(title);

    // TODO: Left Panel
    //leftPanel.setPreferredSize(new Dimension(120, 0));
    leftPanel.setBackground(color2);
    leftPanel.setBorder(new MatteBorder(0, 0, 0, 4, Color.GRAY));

    leftPanelScrollPane = new JScrollPane();
    leftPanelScrollPane.setPreferredSize(new Dimension(120, 600));
    leftPanelScrollPane.setHorizontalScrollBar(null);
    leftPanelScrollPane.setBorder(null);
    leftPanelScrollPane.setViewportView(leftPanel);
    leftPanelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    leftPanelScrollPane.getVerticalScrollBar().setEnabled(false);
    add(leftPanelScrollPane, BorderLayout.WEST);

    // SETUP TICKER BUTTONS
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 0, 5, 0);
    int i = 0;
    gbc.gridx = 0;
    for (Map.Entry<String, String> company : dMan.getTickers().entrySet()) {
      gbc.gridy = i;
      i++;
      leftPanel.add(new TickerButton(company.getKey()), gbc);
    }

    // TODO: Bottom Bar
    bottomBar.setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), 150));
    bottomBar.setBackground(color2);
    add(bottomBar, BorderLayout.SOUTH);

    Dimension bottomBarDim = bottomBar.getPreferredSize();

    addNewCompanyButton = new JButton("New Company");
    addNewCompanyButton.setFocusable(false);
    addNewCompanyButton.setBackground(bgColor);
    addNewCompanyButton.setBounds((int) leftPanel.getPreferredSize().getWidth(), (int) centerPanel.getPreferredSize().getHeight() + 10, 150, 30);
    addNewCompanyButton.setForeground(Color.WHITE);
    addNewCompanyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    addNewCompanyButton.setBorderPainted(false);
    bottomBar.add(addNewCompanyButton);

    // TODO: CENTER PANEL
    centerPanel.setBackground(bgColor);
    centerPanel.setBorder(new MatteBorder(0, 0, 4, 0, Color.GRAY));
    add(centerPanel, BorderLayout.CENTER);

    stockGraph.setBackground(bgColor);
    stockGraph.setBounds(100, 25, 560, 425);
    stockGraph.setDoubleBuffered(true);
    centerPanel.add(stockGraph);

    JLabel dotsToggleLabel = new JLabel("Dots");
    dotsToggleLabel.setBounds(100, 450, 60, 30);
    dotsToggleLabel.setForeground(Color.WHITE);
    centerPanel.add(dotsToggleLabel);

    JButton toggleDotsButton = new JButton(new ImageIcon(getClass().getResource("/switch_off.png")));
    toggleDotsButton.setBounds(130,450, 30,30);
    toggleDotsButton.setBorderPainted(false);
    toggleDotsButton.setContentAreaFilled(false);
    toggleDotsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
    centerPanel.add(toggleDotsButton);

    DecimalFormat df = new DecimalFormat("0.00");

    balanceLabel = new JLabel("Balance: $" + df.format(user.getBalance()));
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
    buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    centerPanel.add(buyButton);

    JButton sellButton = new JButton("Sell: " + unitsSelected + "x");
    sellButton.setBorder(new MatteBorder(3, 0, 0, 0, bgColor));
    sellButton.setBackground(color2);
    sellButton.setFocusable(false);
    sellButton.setForeground(Color.WHITE);
    sellButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80, 480, 80, 30);
    sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    centerPanel.add(sellButton);

    JButton incrementUpButton = new JButton("+");
    incrementUpButton.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    incrementUpButton.setForeground(Color.WHITE);
    incrementUpButton.setBackground(color2);
    incrementUpButton.setFocusable(false);
    incrementUpButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80 - 30, 450, 30, 30);
    incrementUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    centerPanel.add(incrementUpButton);

    JButton incrementDownButton = new JButton("-");
    incrementDownButton.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    incrementDownButton.setForeground(Color.WHITE);
    incrementDownButton.setBackground(color2);
    incrementDownButton.setFocusable(false);
    incrementDownButton.setBounds(100 + (int) stockGraph.getBounds().getWidth() - 80 - 30, 480, 30, 30);
    incrementDownButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    centerPanel.add(incrementDownButton);

    unitsOwnedLabel = new JLabel("Shares Owned: 0");
    unitsOwnedLabel.setForeground(Color.WHITE);
    unitsOwnedLabel.setBounds((int) stockGraph.getBounds().getWidth() - 160, 450, 160, 30);
    unitsOwnedLabel.setOpaque(true);
    unitsOwnedLabel.setBackground(color2);
    unitsOwnedLabel.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    unitsOwnedLabel.setHorizontalAlignment(JLabel.CENTER);
    centerPanel.add(unitsOwnedLabel);

    JTextField selectUnitsField = new JTextField(15);
    selectUnitsField.setBounds((int) incrementDownButton.getBounds().getX(), 510, 113, 25);
    selectUnitsField.setBackground(color2);
    selectUnitsField.setBorder(new MatteBorder(3, 3, 0, 3, bgColor));
    selectUnitsField.setForeground(Color.WHITE);
    selectUnitsField.setCaretColor(Color.WHITE);
    selectUnitsField.setFont(font1.deriveFont(Font.BOLD, 12));
    selectUnitsField.setHorizontalAlignment(JTextField.CENTER);
    centerPanel.add(selectUnitsField);

    setupTradingButtons(buyButton, sellButton, incrementUpButton, incrementDownButton, selectUnitsField);
    setupListeners();
  }

  public class TickerButton extends JButton {
    public TickerButton(String text) {
      setText(text);
      setFont(font1.deriveFont(Font.BOLD, 20));
      setForeground(Color.WHITE);
      setBackground(color2);
      setFocusable(false);
      //setBorderPainted(false);
      setPreferredSize(new Dimension((int) leftPanelScrollPane.getPreferredSize().getWidth() - 4, 45));
      setBorder(new MatteBorder(1, 0, 1, 0, Color.GRAY));

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
          unitsOwnedLabel.setText("Shares Owned: " + dMan.getCompany(selectedCompany).getUnitsOwned());
        }
      });

    }
  }

  public void displayData(Company c, Graphics2D g2) {
    // TODO: ERASE
    stockGraph.paint(g2);

    // TODO: SETUP GRAPHICS
    g2.setColor(Color.GREEN);
    g2.setStroke(new BasicStroke(3));
    g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

    // TODO: GRAPH BACKGROUND
    Rectangle graphBounds = stockGraph.getBounds();
    g2.setColor(color2);
    g2.fillRoundRect(0, 0, graphBounds.width, graphBounds.height, 10, 10);

    // TODO: COLOR BASED ON MOST RECENT VALUE
    LinkedList<double[]> data = c.data;
    if (data.size() > 1) {
      if (c.getCurrentValue() <= data.get(data.size() - 2)[1]) {
        g2.setColor(Color.RED);
      } else {
        g2.setColor(Color.GREEN);
      }
    } else {
      g2.setColor(Color.GREEN);
    }

    // TODO: CALCULATE X INCREMENT OF POINTS
    double increment = stockGraph.getBounds().getWidth() / data.size();

    // TODO: Create 2D Array Of Points
    int[][] pts = new int[data.size()][data.size()];

    int x = 0; // x coordinate of point
    int i = 0; // which point is being drawn
    // TODO: FILL IN POINTS & DOTS IF ENABLED
    if (data.size() > 1) {
      for (double[] pair : data) {
        if (dotsEnabled) {
          g2.fillOval(x - 3, (int) stockGraph.getBounds().getHeight() - (int) pair[1] - 3, 6, 6);
        }
        pts[i][0] = x;
        pts[i][1] = (int) stockGraph.getBounds().getHeight() - (int) pair[1];
        x += increment;
        i++;
      }
    }

    // TODO: DRAW LINES BETWEEN POINTS
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
    g2.setColor(Color.ORANGE);
    g2.drawString("$" + df.format(c.data.get(c.data.size() - 1)[1]), 5, 65);

    // $ UP/DOWN SINCE OPEN
    try {
      if (c.getCurrentValue() < c.getOpening()) {
        double upDown = ((c.getOpening() / c.getCurrentValue()) * 100);
        System.out.println("\nOpening: " + c.getOpening());
        System.out.println("Current: " + c.getCurrentValue());
        System.out.println("Up/Down: -" + (c.getCurrentValue() / c.getOpening() * 100) + "%");

        g2.setColor(Color.RED);
        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/down.png")), 0, 68, null);
        g2.drawString("-" + df.format(upDown) + "%", 30, 90);
      } else {
        double upDown = ((c.getCurrentValue() / c.getOpening()) * 100) - 100;

        g2.setColor(Color.GREEN);
        g2.drawImage(ImageIO.read(getClass().getResourceAsStream("/up.png")), 0, 68, null);
        g2.drawString(df.format(upDown) + "%", 30, 90);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // TODO: OPENING PRICE
    g2.setColor(Color.WHITE);
    String opening = "Opening: $" + df.format(c.getOpening());
    int textWidth = (int) g2.getFontMetrics().getStringBounds(opening, g2).getWidth();
    g2.drawString(opening, (int) stockGraph.getBounds().getWidth() - textWidth - 10, 20);
  }

  public class UpdateThread extends Thread {
    @Override
    public void run() {
      while (true) {
        try {
          Graphics2D g2 = (Graphics2D) stockGraph.getGraphics();
          if (selectedCompany != null) {
            stockGraph.paint(g2);
            dMan.getCompany(selectedCompany).addData();
            displayData(dMan.getCompany(selectedCompany), g2);
          }
//          else {
//            stockGraph.paint(g2);
//            String instructionText = "Select A Company";
//            int textWidth = (int) g2.getFontMetrics().getStringBounds(instructionText, g2).getWidth();
//            g2.drawString(instructionText, (int) stockGraph.getBounds().getWidth() / 2 - (textWidth / 2), (int) stockGraph.getBounds().getHeight() / 2);
//          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          sleep(updateTime * 1000);
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
          unitsOwnedLabel.setText("Shares Owned: " + company.getUnitsOwned());
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
          unitsOwnedLabel.setText("Shares Owned: " + dMan.getCompany(selectedCompany).getUnitsOwned());
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

  public void setupListeners() {
    addNewCompanyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addCompany();
      }
    });
  }

  public void addCompany() {
    try {
      String companyName = JOptionPane.showInputDialog(this, "Enter Company Name");
      String companyTicker = JOptionPane.showInputDialog(this, "Enter Ticker Symbol").toUpperCase();

      while (companyTicker.length() > 5) {
        JOptionPane.showMessageDialog(this, "Ticker is too long");
        companyTicker = JOptionPane.showInputDialog(this, "Enter Ticker Symbol").toUpperCase();
      }

      dMan.addCompanyAndTicker(companyName, companyTicker);

      JOptionPane.showMessageDialog(this, "Created " + companyName + " [" + companyTicker + "]");
      leftPanel.add(new TickerButton(companyTicker));
      leftPanel.revalidate();
      leftPanel.repaint();
    } catch (Exception e) {

    }
  }
}
