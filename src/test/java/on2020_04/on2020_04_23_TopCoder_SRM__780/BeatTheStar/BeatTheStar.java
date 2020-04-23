package on2020_04.on2020_04_23_TopCoder_SRM__780.BeatTheStar;



import template.utils.Debug;
import template.utils.SequenceUtils;

public class BeatTheStar {
   // Debug debug = new Debug(true);

    public double doesItMatter(int N, int G) {
        int[] score = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            score[i] = i;
        }
        score[G] = 0;
        int zero = 10000;
        double[][] dp = new double[N + 1][zero * 2];
        dp[0][zero + G] = 1;
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j < zero * 2; j++) {
                if (j - score[i] >= 0) {
                    dp[i][j] += dp[i - 1][j - score[i]] * 0.5;
                }
                if (j + score[i] < zero * 2) {
                    dp[i][j] += dp[i - 1][j + score[i]] * 0.5;
                }
            }
        }
       // debug.debug("score", score);
      //  debug.debug("dp", dp);
        double ans = 0;
        for (int i = zero; i <= zero + G * 2; i++) {
            ans += dp[N][i];
        }

        return ans;
    }
}
