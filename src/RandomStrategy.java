import java.util.Random;

/**
 * Random strategy: computer picks Rock, Paper, or Scissors at random.
 */
public class RandomStrategy implements Strategy {
    private final Random random = new Random();

    @Override
    public String getMove(String playerMove) {
        int choice = random.nextInt(3);
        if (choice == 0) return "R";
        if (choice == 1) return "P";
        return "S";
    }
}
