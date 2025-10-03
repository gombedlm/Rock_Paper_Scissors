import java.util.Random;

public class RandomStrategy implements Strategy {
    private final Random rand = new Random();

    @Override
    public String getMove(String playerMove) {
        int n = rand.nextInt(3);
        switch (n) {
            case 0: return "R";
            case 1: return "P";
            case 2: return "S";
            default: return "R";
        }
    }
}
