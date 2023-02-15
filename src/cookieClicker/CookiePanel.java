package cookieClicker;

import cookieClicker.cookiemakers.CookieMaker;
import cookieClicker.cookiemakers.Grandma;
import stockTrading.Main;
import stockTrading.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class CookiePanel extends JPanel {

    // TODO: FRAME
    private JFrame window;

    // TODO: BUTTONS/LABELS
    private JButton cookieButton;
    private JLabel cookiesLabel;
    private JLabel cpsLabel;

    // TODO: PANELS
    private JPanel rightPanel;

    // TODO: GENERAL FIELDS
    private int cookies = 0;

    // TODO: UTILITIES
    private DataManager dm = new DataManager(this);

    // TODO: USER INFO
    private static ArrayList<CookieMaker> makers = new ArrayList<CookieMaker>();

    public CookiePanel(JFrame window) {
        // DATA HANDLING
        dm.loadData();

        this.window = window;
        setPreferredSize(new Dimension(900, 650));
        setBackground(Color.RED);
        setLayout(new BorderLayout());

        // TODO: THREAD
        new AddCookiesThread().start();

        // TODO: TOP BAR
        JPanel topBar = new JPanel(null);
        topBar.setPreferredSize(new Dimension(0, 40));
        topBar.setBackground(new Color(93, 51, 7));

        JButton stocksButton = new JButton("Back");
        stocksButton.setBounds(0, 0, 80, 30);
        stocksButton.setContentAreaFilled(false);
        stocksButton.setBorderPainted(false);
        stocksButton.setFocusable(false);
        stocksButton.setForeground(Color.WHITE);
        topBar.add(stocksButton);
        stocksButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dm.saveData();
                removeThisPanel();
                window.add(new MainPanel(window));
                window.revalidate();
                window.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


        add(topBar, BorderLayout.NORTH);

        // TODO: LEFT PANEL
        JPanel leftPanel = new JPanel(null);
        leftPanel.setPreferredSize(new Dimension(300, (int) getPreferredSize().getHeight()));
        add(leftPanel, BorderLayout.WEST);
        leftPanel.setBackground(Color.BLUE);

//        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/cookieClicker/bgBlue1.png")));
//        background.setBounds(0, 0, (int) leftPanel.getPreferredSize().getWidth(), (int) leftPanel.getPreferredSize().getHeight());
//        leftPanel.add(background);

        cookiesLabel = new JLabel("Cookies: " + cookies);
        cookiesLabel.setBounds(0, 40, (int) leftPanel.getPreferredSize().getWidth(), 40);
        cookiesLabel.setFont(new Font("DIALOG", Font.BOLD, 40));
        cookiesLabel.setHorizontalAlignment(JLabel.CENTER);
        cookiesLabel.setForeground(Color.WHITE);
        cookiesLabel.setBackground(Color.RED);
        leftPanel.add(cookiesLabel);

        cpsLabel = new JLabel("CPS: " + getCPS());
        cpsLabel.setBounds(0, 80, (int) leftPanel.getPreferredSize().getWidth(), 40);
        cpsLabel.setFont(new Font("DIALOG", Font.PLAIN, 25));
        cpsLabel.setHorizontalAlignment(JLabel.CENTER);
        cpsLabel.setForeground(Color.WHITE);
        leftPanel.add(cpsLabel);

        cookieButton = new JButton(new ImageIcon(getClass().getResource("/cookieClicker/cookie.png")));
        cookieButton.setBounds((int) (leftPanel.getPreferredSize().getWidth() / 2) - 110, (int) (leftPanel.getPreferredSize().getHeight() / 2.5) - 110, 220, 220);
        cookieButton.setContentAreaFilled(false);
        cookieButton.setBorderPainted(false);
        cookieButton.setFocusable(false);
        cookieButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cookies++;
                cookiesLabel.setText("Cookies: " + cookies);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        leftPanel.add(cookieButton);

        // TODO: RIGHT PANEL
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(200, (int) getPreferredSize().getHeight()));

        rightPanel.add(new CookieMakerBuyButton(new cookieClicker.cookiemakers.Cursor()));
        rightPanel.add(new CookieMakerBuyButton(new Grandma()));

        add(rightPanel, BorderLayout.EAST);


        // TODO: CENTER PANEL
        JPanel centerPanel = new JPanel(null);
        centerPanel.setBackground(new Color(187, 157, 82));
        add(centerPanel, BorderLayout.CENTER);
    }

    public void removeThisPanel() {
        Main.removePanel(this);
    }

    public int getCookies() {
        return cookies;
    }

    public void setCookies(int amt) {
        cookies = amt;
    }


    public class CookieMakerBuyButton extends JPanel {
        public CookieMakerBuyButton(CookieMaker cMaker) {
            setLayout(null);
            setPreferredSize(new Dimension((int) rightPanel.getPreferredSize().getWidth(), 63));
            setBackground(Color.LIGHT_GRAY);

            JLabel picLabel = new JLabel(cMaker.getImage());
            picLabel.setBounds(0, 0, 63, 63);
            add(picLabel);

            JLabel nameLabel = new JLabel(cMaker.getName());
            nameLabel.setBounds(70, 0, 200, 20);
            add(nameLabel);

            JLabel priceLabel = new JLabel(cMaker.getPrice() + " cookies");
            priceLabel.setBounds(70, 20, 200, 20);
            add(priceLabel);

            // TODO: LISTENERS
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (cookies >= cMaker.getPrice()) {
                        cookies -= cMaker.getPrice();
                        cookiesLabel.setText("Cookies: " + cookies);
                        makers.add(cMaker);
                        cpsLabel.setText("CPS: " + getCPS());
                    } else {
                        JOptionPane.showMessageDialog(null, "You cannot afford this");
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }
    }

    public class AddCookiesThread extends Thread {
        public void run() {
            while (true) {
                try {
                    for (CookieMaker cMaker : makers) {
                        cookies += cMaker.getProduction();
                        cookiesLabel.setText("Cookies: " + cookies);
                    }

                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCPS() {
        int cps = 0;
        for (CookieMaker cm : makers) {
            cps += cm.getProduction();
        }
        return cps;
    }

    public static ArrayList<CookieMaker> getMakers() {
        return makers;
    }

    public void addMakers(String name, int count) {

        switch (name) {
            case "Cursor":
                for (int i = 0; i < count; i++) {
                    makers.add(new cookieClicker.cookiemakers.Cursor());
                }
                break;
            case "Grandma":
                for (int i = 0; i < count; i++) {
                    makers.add(new Grandma());
                }
                break;
        }
    }
}
