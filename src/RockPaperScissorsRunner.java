/**
 * Runs the Rock Paper Scissors game.
 */
public class RockPaperScissorsRunner {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new RockPaperScissorsFrame().setVisible(true);
        });
    }
}
