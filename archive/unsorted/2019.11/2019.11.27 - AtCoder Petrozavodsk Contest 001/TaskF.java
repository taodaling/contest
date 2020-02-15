package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] xor = new int[n];
        for (int i = 1; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int a = in.readInt();
            xor[x] ^= a;
            xor[y] ^= a;
        }

        int[] cnts = new int[16];
        for (int i = 0; i < n; i++) {
            cnts[xor[i]]++;
        }

        int cc = 0;
        cc += cnts[0];
        cnts[0] = 0;
        for (int i = 1; i < 16; i++) {
            cc += cnts[i] / 2;
            cnts[i] %= 2;
        }

        BitOperator bo = new BitOperator();
        SubsetGenerator sg = new SubsetGenerator();
        int[] dp = new int[1 << 16];

        int mask = 0;
        for (int i = 0; i < 16; i++) {
            if (cnts[i] == 1) {
                mask = bo.setBit(mask, i, true);
            }
        }

        for (int i = 1; i < dp.length; i++) {
            if (!bo.subset(i, mask)) {
                dp[i] = 0;
                continue;
            }

            int x = 0;
            for (int j = 0; j < 16; j++) {
                if (bo.bitAt(i, j) == 1) {
                    x ^= j;
                }
            }
            if (x != 0) {
                continue;
            }
            dp[i] = 1;
            sg.setSet(i);
            while (sg.hasNext()) {
                int next = sg.next();
                if (next == 0 || next == i) {
                    continue;
                }
                dp[i] = Math.max(dp[i], dp[next] + dp[i - next]);
            }
        }

        cc += dp[mask];
        int edge = n - cc;
        out.println(edge);
    }
}
