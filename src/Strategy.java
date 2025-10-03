/**
 * Strategy interface for computer move selection.
 */
public interface Strategy {
    /**
     * Gets the computer's move based on the strategy.
     * @param playerMove the player's last move
     * @return the computer's move ("R", "P", or "S")
     */
    String getMove(String playerMove);
}
