package contest;

import template.math.Modular;
import template.math.Power;

public class StrawberryHard {
    public int competitive(int n, int k, int Arange, int Brange, int seed) {
        int[] A = new int[2 * k + 1];
        int[] B = new int[2 * k + 1];

        long state = seed;
        for (int i = 0; i <= 2 * k; i++) {
            state = (1103515245 * state + 12345);
            state = state % (1L << 31);
            A[i] = (int) (state % Arange);
            state = (1103515245 * state + 12345);
            state = state % (1L << 31);
            B[i] = (int) (state % Brange);
        }

        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int sumA = 0;
        int sumB = 0;
        for (int i = 0; i <= 2 * k; i++) {
            sumA = mod.plus(sumA, A[i]);
            sumB = mod.plus(sumB, B[i]);
        }
        int invSumA = power.inverseByFermat(sumA);
        int invSumB = power.inverseByFermat(sumB);
        for (int i = 0; i <= 2 * k; i++) {
            A[i] = mod.mul(A[i], invSumA);
            B[i] = mod.mul(B[i], invSumB);
        }

        int zero = k;
        int limit = 2 * k;
        int[][] dp = new int[n + 1][limit + 1];
        dp[0][zero] = 1;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                //A +
                for (int j = 0; j <= limit; j++) {
                    for (int t = 0; t <= limit; t++) {
                        if (j + t <= limit) {
                            int prob = mod.mul(A[t], dp[i][j]);
                            dp[i + 1][j + t] = mod.plus(dp[i + 1][j + t], prob);
                        }
                    }
                }
            } else {
                //B -
                for (int j = 0; j <= limit; j++) {
                    for (int t = 0; t <= limit; t++) {
                        if (j - t >= 0) {
                            int prob = mod.mul(B[t], dp[i][j]);
                            dp[i + 1][j - t] = mod.plus(dp[i + 1][j - t], prob);
                        }
                    }
                }
            }
        }

        int ans = 0;
        for (int p : dp[n]) {
            ans = mod.plus(ans, p);
        }

        return ans;
    }
}
