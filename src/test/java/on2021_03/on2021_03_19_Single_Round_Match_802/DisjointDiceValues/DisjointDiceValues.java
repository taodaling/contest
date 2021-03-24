package on2021_03.on2021_03_19_Single_Round_Match_802.DisjointDiceValues;



import java.util.Arrays;

public class DisjointDiceValues {
    public double getProbability(int A, int B) {
        double[] prev = new double[1 << 6];
        prev[0] = 1;
        double[] next = new double[1 << 6];
        for (int i = 0; i < A; i++) {
            Arrays.fill(next, 0);
            for (int j = 0; j < 1 << 6; j++) {
                for (int k = 0; k < 6; k++) {
                    int to = j | (1 << k);
                    next[to] += prev[j] / 6;
                }
            }
            double[] tmp = prev;
            prev = next;
            next = tmp;
        }
        double ans = 0;
        for (int j = 0; j < 1 << 6; j++) {
            int bc = Integer.bitCount(j);
            ans += prev[j] * Math.pow(1 - bc / 6d, B);
        }
        return 1 - ans;
    }
}
