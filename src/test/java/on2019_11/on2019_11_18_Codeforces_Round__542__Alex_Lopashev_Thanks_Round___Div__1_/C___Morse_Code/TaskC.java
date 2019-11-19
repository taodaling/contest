package on2019_11.on2019_11_18_Codeforces_Round__542__Alex_Lopashev_Thanks_Round___Div__1_.C___Morse_Code;



import template.*;

import java.util.Arrays;

public class TaskC {

    int[][] forbiden = new int[][]{{0, 0, 1, 1},
            {0, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}};

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        Hash h11 = new Hash(m + 4, 11);
        Hash h31 = new Hash(m + 4, 31);

        h11.populate(forbiden[0], 4);
        h31.populate(forbiden[0], 4);
        long f1 = DigitUtils.asLong(h11.hashVerbose(0, 3), h31.hashVerbose(0, 3));
        h11.populate(forbiden[1], 4);
        h31.populate(forbiden[1], 4);
        long f2 = DigitUtils.asLong(h11.hashVerbose(0, 3), h31.hashVerbose(0, 3));
        h11.populate(forbiden[2], 4);
        h31.populate(forbiden[2], 4);
        long f3 = DigitUtils.asLong(h11.hashVerbose(0, 3), h31.hashVerbose(0, 3));
        h11.populate(forbiden[3], 4);
        h31.populate(forbiden[3], 4);
        long f4 = DigitUtils.asLong(h11.hashVerbose(0, 3), h31.hashVerbose(0, 3));

        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        LongHashSet set = new LongHashSet(m * (m - 1) / 2 + m);
        int[] str = new int[m];
        int[] dp = new int[m + 1];
        int ans = 0;
        for (int i = 0; i < m; i++) {
            str[i] = in.readInt();
            h11.populate(str, i + 1);
            h31.populate(str, i + 1);
            Arrays.fill(dp, 0);
            for (int j = 0; j <= i; j++) {
                long sig = DigitUtils.asLong(h11.hashVerbose(j, i),
                        h31.hashVerbose(j, i));
                if (set.contain(sig)) {
                    continue;
                }
                set.add(sig);
                dp[j] = 1;
            }
            for (int j = 0; j <= i; j++) {
                if (j + 1 <= i + 1) {
                    dp[j + 1] = mod.plus(dp[j + 1], dp[j]);
                }
                if (j + 2 <= i + 1) {
                    dp[j + 2] = mod.plus(dp[j + 2], dp[j]);
                }
                if (j + 3 <= i + 1) {
                    dp[j + 3] = mod.plus(dp[j + 3], dp[j]);
                }
                long h = DigitUtils.asLong(h11.hashVerbose(j, j + 3),
                        h31.hashVerbose(j, j + 3));
                if (j + 4 <= i + 1 && !(h == f1 || h == f2 || h == f3 || h == f4)) {
                    dp[j + 4] = mod.plus(dp[j + 4], dp[j]);
                }
            }
            ans = mod.plus(ans, dp[i + 1]);
            out.println(ans);
        }

        return;
    }

    public boolean forbiden(int[] s, int l, int r) {
        for (int[] c : forbiden) {
            if (SequenceUtils.equal(s, l, r, c, 0, 3)) {
                return true;
            }
        }
        return false;
    }
}
