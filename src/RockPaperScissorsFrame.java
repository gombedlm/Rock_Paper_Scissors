import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Main GUI for the Rock Paper Scissors game.
 */
public class RockPaperScissorsFrame extends JFrame {

    private JTextArea resultsArea;
    private JTextField playerWinsField, computerWinsField, tiesField;

    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;
    private String lastPlayerMove = null;

    // Strategies
    private final Strategy cheat = new Cheat();
    private final Strategy random = new RandomStrategy();
    private final Strategy leastUsed = new LeastUsed();
    private final Strategy mostUsed = new MostUsed();
    private final Strategy lastUsed = new LastUsed();

    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Move"));

        JButton rockButton = new JButton(loadIcon("/Rock.png", "Rock"));
        JButton paperButton = new JButton(loadIcon("/Paper.png", "Paper"));
        JButton scissorsButton = new JButton(loadIcon("/Scissors.png", "Scissors"));
        JButton quitButton = new JButton(loadIcon("/Quit.png", "Quit"));

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.NORTH);

        // --- Stats Panel ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 6));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Game Stats"));

        statsPanel.add(new JLabel("Player Wins:"));
        playerWinsField = new JTextField("0", 5);
        playerWinsField.setEditable(false);
        statsPanel.add(playerWinsField);

        statsPanel.add(new JLabel("Computer Wins:"));
        computerWinsField = new JTextField("0", 5);
        computerWinsField.setEditable(false);
        statsPanel.add(computerWinsField);

        statsPanel.add(new JLabel("Ties:"));
        tiesField = new JTextField("0", 5);
        tiesField.setEditable(false);
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.SOUTH);

        // --- Results Area ---
        resultsArea = new JTextArea(10, 40);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.CENTER);

        // --- Action Listeners ---
        ActionListener listener = e -> {
            String playerMove = "";
            if (e.getSource() == rockButton) playerMove = "R";
            else if (e.getSource() == paperButton) playerMove = "P";
            else if (e.getSource() == scissorsButton) playerMove = "S";

            if (!playerMove.isEmpty()) {
                playRound(playerMove);
            }
        };

        rockButton.addActionListener(listener);
        paperButton.addActionListener(listener);
        scissorsButton.addActionListener(listener);
        quitButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Loads an image from resources and scales it to 64x64 px.
     * If missing, falls back to text button.
     */
    private ImageIcon loadIcon(String path, String fallbackText) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImg = originalIcon.getImage()
                    .getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } else {
            System.err.println("Could not load: " + path + " → Using text: " + fallbackText);
            return null;
        }
    }

    private void playRound(String playerMove) {
        updatePlayerMoveCounts(playerMove);

        Strategy strategy;
        String strategyName;

        int roll = new Random().nextInt(100) + 1;
        if (roll <= 10) {
            strategy = cheat;
            strategyName = "Cheat";
        } else if (roll <= 30) {
            strategy = leastUsed;
            strategyName = "Least Used";
        } else if (roll <= 50) {
            strategy = mostUsed;
            strategyName = "Most Used";
        } else if (roll <= 70) {
            strategy = lastUsed;
            strategyName = "Last Used";
        } else {
            strategy = random;
            strategyName = "Random";
        }

        String computerMove = strategy.getMove(playerMove);
        String outcome = determineWinner(playerMove, computerMove);

        resultsArea.append(outcome + " (Computer: " + strategyName + ")\n");

        lastPlayerMove = playerMove;
        updateStats();
    }

    private void updatePlayerMoveCounts(String move) {
        switch (move) {
            case "R": rockCount++; break;
            case "P": paperCount++; break;
            case "S": scissorsCount++; break;
        }
    }

    private String determineWinner(String player, String computer) {
        if (player.equals(computer)) {
            ties++;
            return toWord(player) + " vs " + toWord(computer) + " (Tie)";
        }

        if ((player.equals("R") && computer.equals("S")) ||
                (player.equals("P") && computer.equals("R")) ||
                (player.equals("S") && computer.equals("P"))) {
            playerWins++;
            return toWord(player) + " beats " + toWord(computer) + " (Player Wins)";
        } else {
            computerWins++;
            return toWord(computer) + " beats " + toWord(player) + " (Computer Wins)";
        }
    }

    private void updateStats() {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    private String toWord(String move) {
        switch (move) {
            case "R": return "Rock";
            case "P": return "Paper";
            case "S": return "Scissors";
            default: return "?";
        }
    }

    // --- Inner Classes for Strategies ---
    private class LeastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (rockCount <= paperCount && rockCount <= scissorsCount) return "P"; // beat Rock
            if (paperCount <= rockCount && paperCount <= scissorsCount) return "S"; // beat Paper
            return "R"; // beat Scissors
        }
    }

    private class MostUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (rockCount >= paperCount && rockCount >= scissorsCount) return "P"; // beat Rock
            if (paperCount >= rockCount && paperCount >= scissorsCount) return "S"; // beat Paper
            return "R"; // beat Scissors
        }
    }

    private class LastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (lastPlayerMove == null) {
                return random.getMove(playerMove);
            }
            switch (lastPlayerMove) {
                case "R": return "P";
                case "P": return "S";
                case "S": return "R";
                default: return random.getMove(playerMove);
            }
        }
    }
}
