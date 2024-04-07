import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Options extends JFrame {
    private JPanel panel;
    private JLabel label1;
    private JLabel label2;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button;

    private static Options instance;

    private int enemiesPerRow= 24;
    private int enemySpeed = 15;
    public static Options getInstance() {
        if (instance == null) {
            instance = new Options();
        }
        return instance;
    }

    public Options() {

        setSize(400, 200);
        setTitle("Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        label1 = new JLabel("Number of aliens:");
        panel.add(label1);

        textField1 = new JTextField();
        panel.add(textField1);

        label2 = new JLabel("Aliens's speed:");
        panel.add(label2);

        textField2 = new JTextField();
        panel.add(textField2);

        button = new JButton("Save");
        panel.add(button);

        button.addActionListener(e -> {
            try {
                if (!textField1.getText().isEmpty()) {
                    enemiesPerRow = Integer.parseInt(textField1.getText());
                }

                if (!textField2.getText().isEmpty()) {
                    enemySpeed = Integer.parseInt(textField2.getText());
                }

                this.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Only numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        this.dispose();});


        add(panel);
        setVisible(true);
    }

    public int getEnemiesPerRow() {
        return enemiesPerRow;
    }

    public int getEnemySpeed() {
        return enemySpeed;
    }
}
