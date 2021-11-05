package on2021_10.on2021_10_27_TopCoder_SRM__817.Hagar;



import java.util.Arrays;

public class Hagar {
    public double expectedProfit(int[] gold) {
        int n = gold.length;
        Arrays.sort(gold);
        int min = gold[0];
        int max = gold[n - 1];
        if (min == max) {
            return (double) min * (gold.length - 1) / gold.length;
        }
        int second = 0;
        int maxCnt = 0;
        for (int i = 0; i < n; i++) {
            if (gold[i] < max) {
                second = gold[i];
            }else{
                maxCnt++;
            }
        }

        long cand1 = second;
        double cand2 = (double)max * (maxCnt - 1) / maxCnt;
        return Math.max(cand1, cand2);
    }
}
