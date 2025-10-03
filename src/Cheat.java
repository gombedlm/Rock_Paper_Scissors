public class Cheat implements Strategy {

    @Override
    public String getMove(String playerMove) {
        switch (playerMove) {
            case "R": return "P"; // Paper beats Rock
            case "P": return "S"; // Scissors beats Paper
            case "S": return "R"; // Rock beats Scissors
            default: return "X";
        }
    }
}
