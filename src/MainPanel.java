import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class MainPanel extends JPanel {

    // TODO: Colors
    private Color bgColor = new Color(47, 47, 47);
    private Color color2 = new Color(67, 67, 67);

    // TODO: Fonts
    private Font font1 = new Font("DIALOG", Font.PLAIN, 12);

    // TODO: Internal Panels
    private JPanel topBar = new JPanel();
    private JPanel leftPanel = new JPanel();

    // TODO: Utilities
    DataManager dMan = new DataManager();

    public MainPanel() {
        // TODO: Initial Setup
        setPreferredSize(new Dimension(900, 600));
        setBackground(bgColor);
        setLayout(new BorderLayout());
        dMan.loadCompanies();

        for (Map.Entry<String, String> company : dMan.getCompanies().entrySet()) {
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

        for (Map.Entry<String, String> company : dMan.getCompanies().entrySet()) {
            leftPanel.add(new TickerButton(company.getKey()));
        }

        add(leftPanel, BorderLayout.WEST);
    }

    public class TickerButton extends JButton {
        public TickerButton(String text) {
            setText(text);
            setFont(font1.deriveFont(Font.BOLD, 24));
            setForeground(Color.WHITE);
            setBackground(color2);
            setFocusable(false);
            setBorderPainted(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    ((TickerButton) e.getSource()).setBackground(Color.RED);
                    e.getComponent().setBackground(Color.RED);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    ((JButton) e.getSource()).setBackground(color2);
                }
            });
        }
    }
}
