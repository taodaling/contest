package contest;

public class EllysCandies {
    public String getWinner(int[] boxes) {
        if (boxes.length % 2 == 0) {
            return "Kris";
        } else {
            return "Elly";
        }
    }
}
