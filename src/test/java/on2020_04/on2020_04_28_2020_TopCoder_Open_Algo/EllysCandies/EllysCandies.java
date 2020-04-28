package on2020_04.on2020_04_28_2020_TopCoder_Open_Algo.EllysCandies;



public class EllysCandies {
    public String getWinner(int[] boxes) {
        if (boxes.length % 2 == 0) {
            return "Kris";
        } else {
            return "Elly";
        }
    }
}
