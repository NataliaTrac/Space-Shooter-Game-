import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class UI extends JFrame {
    private JButton buttonStart;
    private JButton buttonOptions;
    private JButton buttonRanking;
    private JButton exit;
    private Player player;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JMenu jMenuStart;
    private JMenu jMenuOptions;
    private ButtonGroup buttonGroup;
    private JMenuBar jMenu;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel5;
    private JPanel jPanel8;
    private JRadioButton ship1;
    private JRadioButton ship2;
    private JRadioButton ship3;
    private JTextField jTextField2;
    private static String RANKING_FILE = "ranking.txt";
    private Ranking ranking;
    private static String selectedImagePath;

    public UI() {
        ranking = loadRankingFromFile(RANKING_FILE);
        initComponents();
        createRankingFileIfNotExists(RANKING_FILE);

    }

    private void initComponents() {
//inicjalizowanie komponentów
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel3 = new JPanel();
        jPanel5 = new JPanel();
        jLabel4 = new JLabel();
        jTextField2 = new JTextField();
        jLabel5 = new JLabel();
        jPanel8 = new JPanel();
        ship1 = new JRadioButton("Ship 1");
        ship2 = new JRadioButton("Ship 2");
        ship3 = new JRadioButton("Ship 3");
        player = new Player();


        ImageIcon imageIcon4 = new ImageIcon("src/images/ship1.png");
        ship1.setIcon(imageIcon4);
        ImageIcon imageIcon5 = new ImageIcon("src/images/ship2.png");
        ship2.setIcon(imageIcon5);
        ImageIcon imageIcon6 = new ImageIcon("src/images/ship3.png");
        ship3.setIcon(imageIcon6);

        //LISTENERY DO JRADIOBUTTONOW
        ship1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedImagePath = "src/images/ship1.png";
            }
        });
        ship2.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedImagePath = "src/images/ship2.png";
            }
        });
        ship3.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectedImagePath = "src/images/ship3.png";
            }
        });


        setLocation(100, 100);

        JPanel buttons = new JPanel();

        buttonStart = new javax.swing.JButton();
        buttonOptions = new javax.swing.JButton();
        buttonRanking = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenu = new javax.swing.JMenuBar();
        jMenuStart = new javax.swing.JMenu();
        jMenuOptions = new javax.swing.JMenu();
        buttonGroup = new

                ButtonGroup();
        buttonGroup.add(ship1);
        buttonGroup.add(ship2);
        buttonGroup.add(ship3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setAutoRequestFocus(false);

        setBackground(new java.awt.Color(0, 0, 0));

        getContentPane().setLayout(new java.awt.GridLayout(2, 1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome in game!");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE));

        getContentPane().add(jPanel1);

        jPanel3.setLayout(new java.awt.GridLayout(1, 3, 5, 5));

        jPanel5.setLayout(new java.awt.GridLayout(4, 1));

        jLabel4.setText("Enter your name:");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        jPanel5.add(jLabel4);

        ImageIcon imageIcon = new ImageIcon("src/images/ship1.png");
        Image imageIc = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIc = new ImageIcon(imageIc);
        jLabel1.setIcon(resizedImageIc);

        jTextField2.setForeground(new java.awt.Color(128, 128, 128));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        jPanel5.add(jTextField2);

        jLabel5.setText("Choose your ship\n");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        jPanel5.add(jLabel5);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        jPanel8.setLayout(new java.awt.GridLayout());
        jPanel8.setBorder(new

                EmptyBorder(5, 5, 5, 5));
        ship1.setText("Ship 1");
        jPanel8.add(ship1);

        ship2.setText("Ship 2");
        jPanel8.add(ship2);

        ship3.setText("Ship 3");
        jPanel8.add(ship3);

        jPanel5.add(jPanel8);

        jPanel3.add(jPanel5);

        buttons.setLayout(new java.awt.GridLayout(4, 1, 4, 4));

        buttonStart.setText("START");
        buttonStart.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        buttonStart.addActionListener(this::startListener);
        buttons.add(buttonStart);

        buttonOptions.setText("OPTIONS");
        buttonOptions.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        buttonOptions.addActionListener(this::optionsAction);
        buttons.add(buttonOptions);

        buttonRanking.setText("RANKING");
        buttonRanking.setAutoscrolls(true);
        buttonRanking.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        buttonRanking.addActionListener(this::rankingAction);
        buttons.add(buttonRanking);

        exit.setText("EXIT");
        exit.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        exit.addActionListener(this::exitAction);
        buttons.add(exit);

        jPanel3.add(buttons);

        jPanel2.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Game rules:");
        jLabel2.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 36));
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(null));
        jPanel2.add(jLabel2);

        jLabel3.setText("<html>1. To start the game, enter your name, you can select a ship<br/>2. During the game, you can move using the <br/> button in the additional panel or the arrows and spaces on the keyboard.<br/>3. If during the game, if you select the BACK option, your score will not be saved</html>");

        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(null));

        jPanel2.add(jLabel3);

        jPanel3.add(jPanel2);

        getContentPane().

                add(jPanel3);

        jMenuStart.setText("Start");
        jMenu.add(jMenuStart);
        jMenuOptions.setText("Options");
        jMenu.add(jMenuOptions);
        jMenuOptions.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                Options optionsFrame = new Options();
                optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                optionsFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        optionsFrame.setVisible(false);
                        optionsFrame.dispose();
                    }
                });
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
        jMenuStart.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                String playerName = jTextField2.getText().trim();
                if (getSelectedImagePath() == null && playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(UI.this, "Choose your ship and enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (getSelectedImagePath() == null) {
                    JOptionPane.showMessageDialog(UI.this,"Choose your ship", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(UI.this,"Enter your name", "Error", JOptionPane.ERROR_MESSAGE);

                } else {
                    dispose();
                    JFrame gameFrame = new JFrame("Game");
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.setResizable(false);
                    gameFrame.getContentPane().add(new Game(ranking, UI.this, getPlayerName()));

                    gameFrame.getContentPane().setPreferredSize(new Dimension(300, 300));
                    gameFrame.pack();
                    gameFrame.setLocationRelativeTo(null);
                    gameFrame.setVisible(true);
                }

            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
        setJMenuBar(jMenu);
        pack();
    }


    private Ranking loadRankingFromFile(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Ranking ranking = (Ranking) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            return ranking;
        } catch (IOException | ClassNotFoundException e) {
            return new Ranking();
        }
    }

    private void createRankingFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(new Ranking());
                objectOut.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSelectedImagePath() {
        return selectedImagePath;
    }

    private void startListener(java.awt.event.ActionEvent evt) {
        String playerName = jTextField2.getText().trim();
        if (getSelectedImagePath() == null && playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Choose your ship and enter your name", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (getSelectedImagePath() == null) {
            JOptionPane.showMessageDialog(this, "Choose your ship", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your name", "ERROR", JOptionPane.ERROR_MESSAGE);

        } else {
            dispose();
            JFrame gameFrame = new JFrame("Game");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setResizable(false);
            gameFrame.getContentPane().add(new Game(ranking, UI.this, getPlayerName()));

            gameFrame.getContentPane().setPreferredSize(new Dimension(300, 300));
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
        }
    }


    private void optionsAction(java.awt.event.ActionEvent evt) {
        JButton sourceButton = (JButton) evt.getSource();
        if (sourceButton == buttonOptions) {
            Options optionsFrame = new Options();
            optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            optionsFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    optionsFrame.setVisible(false);
                    optionsFrame.dispose();
                }
            });
        }
    }


    private void rankingAction(java.awt.event.ActionEvent evt) {
        ranking = loadRankingFromFile(RANKING_FILE);
        if (ranking != null) {
                JFrame rankingFrame = new JFrame("Ranking");
                rankingFrame.add(ranking);

                rankingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                rankingFrame.setSize(400, 300);
                rankingFrame.setLocationRelativeTo(null);
                rankingFrame.setVisible(true);
                rankingFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        rankingFrame.setVisible(false);
                        rankingFrame.dispose();
                    }
                });
            }
        }



    public String getPlayerName() {
        return jTextField2.getText();
    }


    private void exitAction(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}
