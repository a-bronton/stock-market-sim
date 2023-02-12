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

    public CookiePanel(JFrame window) {
        this.window = window;
        setPreferredSize(new Dimension(900, 650));
        setBackground(Color.RED);
        setLayout(new BorderLayout());

        // TODO: TOPBAR
        JPanel topBar = new JPanel();
        topBar.setPreferredSize(new Dimension(0, 40));
        topBar.setBackground(Color.LIGHT_GRAY);

        JButton stocksButton = new JButton("Back");
        topBar.add(stocksButton);
        stocksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeThisPanel();
                window.add(new MainPanel(window));
                window.revalidate();
                window.repaint();
            }
        });


        add(topBar, BorderLayout.NORTH);


        // TODO: CENTER PANEL
        JPanel centerPanel = new JPanel(null);
        add(centerPanel, BorderLayout.CENTER);

        JLabel cookiesLabel = new JLabel("Cookies: " + cookies);
        cookiesLabel.setBounds(0, 40, (int) getPreferredSize().getWidth(), 40);
        cookiesLabel.setFont(new Font("DIALOG", Font.BOLD, 40));
        cookiesLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(cookiesLabel);

        cookieButton = new JButton(new ImageIcon(getClass().getResource("/cookieClicker/cookie.png")));
        cookieButton.setBounds((int) (getPreferredSize().getWidth() / 2) - 200, (int) (getPreferredSize().getHeight() / 2) - 200, 400, 400);
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
;
        centerPanel.add(cookieButton);
    }

    public void removeThisPanel() {
        Main.removePanel(this);
    }
}
