package contest;

import template.utils.ArrayIndex;
import template.utils.SequenceUtils;

public class DigitStringDiv1 {
    char[] sc;
    char[] xc;

    public int compare(int i, int j) {
        int a = sc[i];
        int b = j >= xc.length ? '0' : xc[j];
        return Integer.compare(a, b);
    }

    public long count(String S, int X) {
        int n = S.length();
        sc = S.toCharArray();
        SequenceUtils.reverse(sc);
        String x = "" + X;
        int m = x.length();
        xc = x.toCharArray();
        if (n < m) {
            return 0;
        }

        SequenceUtils.reverse(xc);
        ArrayIndex ai = new ArrayIndex(n + 1, n + 1, 3, 2);
        long[] dp = new long[ai.totalSize()];
        dp[ai.indexOf(0, 0, 1, 0)] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int t = 0; t < 2; t++) {
                        long way = dp[ai.indexOf(i, j, k, t)];
                        if (way == 0) {
                            continue;
                        }
                        //remove
                        dp[ai.indexOf(i + 1, j, k, t)] += way;
                        //retain
                        int comp = compare(i, j) + 1;
                        if (comp == 1) {
                            comp = k;
                        }
                        dp[ai.indexOf(i + 1, j + 1, comp, sc[i] == '0' ? 0 : 1)] += way;
                    }
                }
            }
        }

        long ans = 0;
        for (int i = m; i <= n; i++) {
            ans += dp[ai.indexOf(n, i, 2, 1)];
        }
        return ans;
    }
}
