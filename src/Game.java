import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game extends JPanel {

    private Dimension d;
    private HighScore currentScore;
    private List<Alien> aliens;
    private JButton backButton, playAgainButton;
    private Player player;
    private Shot shot;
    private static final int MAX_HIGH_SCORES = 10;
    private int direction = -1;
    private int deaths = 0;
    private int finalScore;
    private boolean paused = false;
    private String playerName;
    private JFrame openFrame;
    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";
    private Ranking ranking;
    private Timer timer;
    private ScheduledExecutorService executorService;
    private int enemiesPerRow;
    private int enemySpeed;

    public Game(Ranking ranking, JFrame openFrame, String playerName) {
        this.ranking = ranking;
        this.openFrame = openFrame;
        this.playerName = playerName;
        Options options = Options.getInstance();
        enemiesPerRow = options.getEnemiesPerRow();
        enemySpeed = options.getEnemySpeed();

        initBoard();
        gameInit();
    }


    private void initBoard() {
        addKeyListener(new TAdapter());


        setFocusable(true);
        d = new Dimension(VALUES.BOARD_WIDTH, VALUES.BOARD_HEIGHT);
        setBackground(Color.black);
        //timer, delay=17milisek
        timer = new Timer(VALUES.DELAY, new GameCycle());
        timer.start();

        gameInit();
        ControlFrame controlFrame = new ControlFrame();
        controlFrame.setVisible(true);
    }

    private class ControlFrame extends JFrame {
        private JButton leftButton;
        private JButton rightButton;
        private JButton spaceButton;
        private JButton pauseButton;

        public ControlFrame() {
            setTitle("Controls");
            setSize(300, 100);
            setLocation(320, 500);
            setResizable(false);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pauseButton = new JButton("Pause");
            playAgainButton = new JButton("Play Again");
            leftButton = new JButton("Left");
            rightButton = new JButton("Right");
            spaceButton = new JButton("Space");

            setLayout(new FlowLayout());
            add(leftButton);
            add(rightButton);
            add(spaceButton);
            add(playAgainButton);
            add(pauseButton);
            pauseButton.addActionListener(e -> {
                paused = !paused;
                if (paused) {
                    timer.stop();
                    executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.schedule(() -> {
                        timer.start();

                    }, 10000, TimeUnit.SECONDS);
                } else {
                    if (executorService != null && !executorService.isShutdown()) {
                        executorService.shutdownNow(); //zatrzymanie
                    }
                    timer.start();
                }
            });

            backButton = new JButton("Back");
            backButton.setBounds((VALUES.BOARD_WIDTH - 100) / 2, VALUES.BOARD_WIDTH / 2 + 60, 100, 30);
            backButton.addActionListener(e -> {

                SwingUtilities.getWindowAncestor(Game.this).dispose();
                ControlFrame.this.dispose();
                openFrame.setVisible(true);
            });
            add(backButton);

            playAgainButton.addActionListener(e -> {
                gameInit();
                inGame = true;
                deaths = 0;
                finalScore = 0;
                message = "Game Over";
                timer.start();
                repaint();
                revalidate();

            });

            leftButton.addActionListener(e -> player.keyPressed(new KeyEvent(Game.this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED)));
            rightButton.addActionListener(e -> player.keyPressed(new KeyEvent(Game.this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED)));

            spaceButton.addActionListener(e -> {
                if (inGame && !shot.isVisible()) {
                    shot = new Shot(player.getX(), player.getY());
                }
            });

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    player.getX(); // Stop the player's movement when the control window is closed
                }
            });
        }
    }

    private void gameInit() {

        this.finalScore = 0;

        aliens = new ArrayList<>();
        int spacing = (VALUES.BOARD_WIDTH - 2 * VALUES.BORDER_RIGHT - (enemiesPerRow - 1) * VALUES.ALIEN_WIDTH) / enemiesPerRow;

        int aliensPerRow = enemiesPerRow;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < (aliensPerRow / 6 - 1); col++) {
                Alien alien = new Alien(VALUES.ALIEN_INIT_X + 18 * col, VALUES.ALIEN_INIT_Y + 18 * row, enemySpeed);

                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
    }


    private void drawAliens(Graphics g) {
        aliens.stream()
                .filter(Alien::isVisible)
                .forEach(alien -> g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this));

        aliens.stream()
                .filter(Alien::isDying)
                .forEach(Alien::die);

    }


    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }


    private void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {
        aliens.stream()
                .map(Alien::getBomb)
                .filter(b -> !b.isDestroyed())
                .forEach(b -> g.drawImage(b.getImage(), b.getX(), b.getY(), this));
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {
            g.drawLine(0, VALUES.GROUND,
                    VALUES.BOARD_WIDTH, VALUES.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }


    }


    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, VALUES.BOARD_WIDTH, VALUES.BOARD_HEIGHT);
        g.drawString(message, (VALUES.BOARD_WIDTH - g.getFontMetrics().stringWidth(message)) / 2,
                VALUES.BOARD_WIDTH / 2 + 30);
        g.drawString("Press 'Back' to return", (VALUES.BOARD_WIDTH - g.getFontMetrics().stringWidth("Press 'Back' to return")) / 2,
                VALUES.BOARD_WIDTH / 2 + 60);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, VALUES.BOARD_WIDTH / 2 - 30, VALUES.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, VALUES.BOARD_WIDTH / 2 - 30, VALUES.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);
        HighScore highScore = new HighScore(playerName, finalScore);
        ranking.saveScore(highScore);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (VALUES.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                VALUES.BOARD_WIDTH / 2);


        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.BOLD, 14));
        g.drawString("Final Score:" + finalScore, 20, 20);

        playAgainButton.addActionListener(e -> {
            gameInit();
            inGame = true;
            timer.start();
            repaint();
            revalidate();
        });
    }


    private void update() {

        if (deaths == enemiesPerRow) {

            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        player.act();

        //
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens.stream().filter(Alien::isVisible).collect(Collectors.toList())) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + VALUES.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + VALUES.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        aliens.forEach(alien -> {
            int x = alien.getX();
            if (x >= VALUES.BOARD_WIDTH - VALUES.BORDER_RIGHT && direction != -1) {

                direction = -1;

                aliens.forEach(a2 -> a2.setY(a2.getY() + enemySpeed));
            }

            if (x <= VALUES.BORDER_LEFT && direction != 1) {

                direction = 1;

                aliens.forEach(a -> a.setY(a.getY() + enemySpeed));
            }
        });

        aliens.stream()
                .filter(Alien::isVisible)
                .forEach(alien -> {
                    int y = alien.getY();

                    if (y > VALUES.GROUND - VALUES.ALIEN_HEIGHT) {
                        inGame = false;
                        message = "Invasion!";
                    }
                    alien.act(direction);
                });


        var generator = new Random();

        for (Alien alien : aliens) {

            int shot = generator.nextInt(15);
            Alien.Bomb bomb = alien.getBomb();
            if (shot == VALUES.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + VALUES.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + VALUES.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= VALUES.GROUND - VALUES.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        update();
        calculateScore();
        repaint();
    }

    private void calculateScore() {
        int score = deaths + 1;
        finalScore = score;

        if (!inGame) {
            currentScore = new HighScore(playerName, finalScore);
            ranking.saveScore(currentScore);
            List<HighScore> highScores = readHighScores();
            highScores.add(currentScore);
            highScores.sort(Collections.reverseOrder());
            highScores = highScores.stream().distinct().limit(MAX_HIGH_SCORES).collect(Collectors.toList());
            updateRanking(highScores);
        }
    }


    private List<HighScore> readHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("ranking.txt"))) {
            reader.lines().forEach(line -> {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    HighScore highScore = new HighScore(name, score);
                    highScores.add(highScore);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScores;
    }

    private void updateRanking(List<HighScore> highScores) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("ranking.txt"))) {
            highScores.forEach(highScore -> {
                try {
                    writer.write(highScore.getName() + ":" + highScore.getScore() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }


        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
