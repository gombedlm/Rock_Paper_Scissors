/**
 * Cheat strategy: beats the player's chosen move.
 * Used ~10% of the time.
 */
public class Cheat implements Strategy {

    @Override
    public String getMove(String playerMove) {
        switch (playerMove) {
            case "R": return "P"; // Paper beats Rock
            case "P": return "S"; // Scissors beat Paper
            case "S": return "R"; // Rock beats Scissors
            default: return "R";  // Fallback
        }
    }
}

