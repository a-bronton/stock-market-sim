import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Stock Market Simulator");

        window.add(new MainPanel());

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}