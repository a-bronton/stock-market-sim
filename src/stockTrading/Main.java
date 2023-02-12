package stockTrading;

import javax.swing.*;

public class Main {

    static JFrame window = new JFrame();
    public static void main(String[] args) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Stock Market Simulator");
        window.setResizable(false);
        new Main().setIcon();

        window.add(new MainPanel(window));

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void setIcon() {
        window.setIconImage(new ImageIcon(getClass().getResource("/game_icon.png")).getImage());
    }

    public static void removePanel(JPanel panel) {
        window.remove(panel);
    }
}