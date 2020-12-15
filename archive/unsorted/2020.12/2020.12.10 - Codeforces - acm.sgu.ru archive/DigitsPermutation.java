package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class DigitsPermutation {
    int m;
    int mask;

    public int add(long x, int plus) {
        long y = (x << plus) | (x >> 17 - plus);
        return (int) (y & (1 << 17) - 1);
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        String n = in.rs();
        m = n.length();
        mask = (1 << m) - 1;
        int[] a = new int[m];
        int[] b = new int[m];
        int[][] mul = new int[m][m];
        for (int i = 0; i < m; i++) {
            a[i] = n.charAt(i) - '0';
        }
        long base = 1;
        for (int i = 0; i < m; i++, base *= 10) {
            b[i] = (int) (base % 17);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                mul[i][j] = a[i] * b[j] % 17;
            }
        }

        int[][] dp = new int[m + 1][1 << m];
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 1 << m; j++) {
                if (dp[i][j] == 0) {
                    continue;
                }
                for (int t = 0; t < m; t++) {
                    if (Bits.get(j, t) == 1 || a[i] == 0 && t == m - 1) {
                        continue;
                    }
                    dp[i + 1][j | (1 << t)] |= add(dp[i][j], mul[i][t]);
                }
            }
        }

        debug.debug("dp", dp);
        debug.debug("a", a);
        debug.debug("b", b);
        if (Bits.get(dp[m][mask], 0) == 0) {
            out.println(-1);
            return;
        }

        int curRm = 0;
        int curBit = mask;
        int[] values = new int[m];
        for (int i = m - 1; i >= 0; i--) {
            for (int t = 0; t < m; t++) {
                if (Bits.get(curBit, t) == 0 || t == m - 1 && a[i] == 0) {
                    continue;
                }
                int j = curBit - (1 << t);
                int contrib = add(dp[i][j], mul[i][t]);
                if (Bits.get(contrib, curRm) == 0) {
                    continue;
                }
                for (int z = 0; z < 17; z++) {
                    if (Bits.get(dp[i][j], z) == 1 && (z + mul[i][t]) % 17 == curRm) {
                        curRm = z;
                        break;
                    }
                }
                values[t] = a[i];
                curBit = j;
                break;
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            out.append(values[i]);
        }
    }
}
