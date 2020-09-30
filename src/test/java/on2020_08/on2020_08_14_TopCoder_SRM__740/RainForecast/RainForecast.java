package on2020_08.on2020_08_14_TopCoder_SRM__740.RainForecast;



public class RainForecast {
    public double predictRain(int ilkoProb, int[] deliverProbs) {
        double[] probs = new double[deliverProbs.length + 1];
        probs[0] = ilkoProb / 100d;
        for (int i = 1; i < probs.length; i++) {
            probs[i] = deliverProbs[i - 1] / 100d;
        }
        double same = 1;
        for (double p : probs) {
            same = p * same + (1 - p) * (1 - same);
        }
        return Math.max(same, 1 - same);
    }
}
