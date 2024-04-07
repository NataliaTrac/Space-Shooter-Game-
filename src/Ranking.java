import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ranking extends JPanel {
    private JLabel rankingLabel;
    private JPanel rankingPanel;
    private List<String> highScores;
    private boolean savedScore = false;
    private File plik = new File("ranking.txt");

    public Ranking() {
        initComponents();
        loadRankingData();
        updateRanking();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        rankingLabel = new JLabel("RANKING");
        rankingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        rankingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(rankingLabel);

        rankingPanel = new JPanel();
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
        add(rankingPanel);
    }
    private void loadRankingData() {
        highScores = readHighScores();
    }

    private void updateRanking() {

        rankingPanel.removeAll();
        int maxResults = 10;
        int resultsCount = Math.min(highScores.size(), maxResults);

        for (int i = 0; i < resultsCount; i++) {
            int rank = i + 1;
            String entry = rank + ". " + highScores.get(i);
            JLabel entryLabel = new JLabel(entry);
            entryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            entryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            rankingPanel.add(entryLabel);
            String playerName = extractPlayerName(highScores.get(i));
            JLabel playerNameLabel = new JLabel(playerName);
            playerNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            playerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            rankingPanel.add(playerNameLabel);
        }
        revalidate();
        repaint();
    }
    private List<String> readHighScores() {
        List<String> highScores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(plik))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScores;
    }
    public void saveScore(HighScore highScore) {
        if (savedScore) {
            return;
        }

        String name = highScore.getName();
        int score = highScore.getScore();

        try {
            if (!plik.exists()) {
                plik.createNewFile();
            }

            FileWriter writer = new FileWriter(plik, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.println(highScore.getName() + " " + score);
            highScores.add(highScore.getName() + " " + score);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        savedScore = true;
    }
    private String extractPlayerName(String entry) {
        int spaceIndex = entry.lastIndexOf(' ');
        if (spaceIndex != -1) {
            return entry.substring(0, spaceIndex);
        }
        return "";
    }
}
