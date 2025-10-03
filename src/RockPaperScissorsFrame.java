import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RockPaperScissorsFrame extends JFrame {
    private JTextArea resultsArea;
    private JTextField playerWinsField, computerWinsField, tiesField;
    private int playerWins = 0, computerWins = 0, ties = 0;

    // Track player moves
    private int rockCount = 0, paperCount = 0, scissorsCount = 0;
    private String lastPlayerMove = null;

    // Strategies
    private Strategy cheat = new Cheat();
    private Strategy random = new RandomStrategy();
    private Strategy leastUsed = new LeastUsed();
    private Strategy mostUsed = new MostUsed();
    private Strategy lastUsed = new LastUsed();

    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Move"));

        JButton rockBtn = new JButton(new ImageIcon("rock.png"));
        JButton paperBtn = new JButton(new ImageIcon("paper.png"));
        JButton scissorsBtn = new JButton(new ImageIcon("scissors.png"));
        JButton quitBtn = new JButton("Quit");

        buttonPanel.add(rockBtn);
        buttonPanel.add(paperBtn);
        buttonPanel.add(scissorsBtn);
        buttonPanel.add(quitBtn);

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

        // --- Results Panel ---
        resultsArea = new JTextArea(10, 40);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Action listeners
        ActionListener listener = e -> {
            String playerMove = "";
            if (e.getSource() == rockBtn) playerMove = "R";
            else if (e.getSource() == paperBtn) playerMove = "P";
            else if (e.getSource() == scissorsBtn) playerMove = "S";

            if (!playerMove.isEmpty()) playRound(playerMove);
        };

        rockBtn.addActionListener(listener);
        paperBtn.addActionListener(listener);
        scissorsBtn.addActionListener(listener);

        quitBtn.addActionListener(e -> System.exit(0));
    }

    private void playRound(String playerMove) {
        updatePlayerMoveCounts(playerMove);

        // Pick strategy based on probability
        int roll = new Random().nextInt(100) + 1;
        Strategy strategy;
        String strategyName;

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
        String result = determineWinner(playerMove, computerMove);

        resultsArea.append(result + " (Computer: " + strategyName + ")\n");
        updateStats();
        lastPlayerMove = playerMove;
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
            return moveToString(player) + " vs " + moveToString(computer) + " (Tie)";
        }

        if ((player.equals("R") && computer.equals("S")) ||
                (player.equals("P") && computer.equals("R")) ||
                (player.equals("S") && computer.equals("P"))) {
            playerWins++;
            return moveToString(player) + " beats " + moveToString(computer) + " (Player Wins)";
        } else {
            computerWins++;
            return moveToString(computer) + " beats " + moveToString(player) + " (Computer Wins)";
        }
    }

    private void updateStats() {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    private String moveToString(String move) {
        switch (move) {
            case "R": return "Rock";
            case "P": return "Paper";
            case "S": return "Scissors";
            default: return "?";
        }
    }

    // ----- Inner Classes -----
    private class LeastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (rockCount <= paperCount && rockCount <= scissorsCount) return "P"; // Beat Rock
            if (paperCount <= rockCount && paperCount <= scissorsCount) return "S"; // Beat Paper
            return "R"; // Beat Scissors
        }
    }

    private class MostUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (rockCount >= paperCount && rockCount >= scissorsCount) return "P"; // Beat Rock
            if (paperCount >= rockCount && paperCount >= scissorsCount) return "S"; // Beat Paper
            return "R"; // Beat Scissors
        }
    }

    private class LastUsed implements Strategy {
        @Override
        public String getMove(String playerMove) {
            if (lastPlayerMove == null) return random.getMove(playerMove);
            switch (lastPlayerMove) {
                case "R": return "P";
                case "P": return "S";
                case "S": return "R";
                default: return random.getMove(playerMove);
            }
        }
    }
}
