package cookieClicker;

import stockTrading.Main;
import stockTrading.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CookiePanel extends JPanel {

    // TODO: FRAME
    JFrame window;

    JButton cookieButton;

    // TODO: GENERAL FIELDS
    private int cookies = 0;

    // TODO: UTILIES
    DataManager dm = new DataManager(this);

    public CookiePanel(JFrame window) {
        // DATA HANDLING
        dm.loadData();

        this.window = window;
        setPreferredSize(new Dimension(900, 650));
        setBackground(Color.RED);
        setLayout(new BorderLayout());

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

        JLabel cookiesLabel = new JLabel("Cookies: " + cookies);
        cookiesLabel.setBounds(0, 40, (int) leftPanel.getPreferredSize().getWidth(), 40);
        cookiesLabel.setFont(new Font("DIALOG", Font.BOLD, 40));
        cookiesLabel.setHorizontalAlignment(JLabel.CENTER);
        cookiesLabel.setForeground(Color.WHITE);
        cookiesLabel.setBackground(Color.RED);
        leftPanel.add(cookiesLabel);

        cookieButton = new JButton(new ImageIcon(getClass().getResource("/cookieClicker/cookie.png")));
        cookieButton.setBounds((int) (leftPanel.getPreferredSize().getWidth() / 2) - 200, (int) (leftPanel.getPreferredSize().getHeight() / 3) - 200, 400, 400);
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
}
