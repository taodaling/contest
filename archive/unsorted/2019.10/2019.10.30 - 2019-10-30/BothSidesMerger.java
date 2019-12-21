package contest;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;
import template.SequenceUtils;

public class BothSidesMerger {
    int n;
    int[] a;
    long[][][] dp;
    long lInf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        dp = new long[n][n][3];
        SequenceUtils.deepFill(dp, -1L);

        long ans = -lInf;
        int maxI = -1;
        int maxK = -1;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 3; k++) {
                if (dp(i, n - 1, k) > ans) {
                    ans = dp(i, n - 1, k);
                    maxI = i;
                    maxK = k;
                }
            }
        }

        out.println(ans);
        int[] vals = new int[n];

        trace(vals, maxI, n - 1, maxK);

        IntegerList valDeque = new IntegerList();
        valDeque.addAll(vals, maxI, n - maxI);


        IntegerList op = new IntegerList(n);
        for (int i = 0; i < maxI; i++) {
            op.add(1);
        }

        while (valDeque.tail() == 0) {
            op.add(valDeque.size());
            valDeque.pop();
        }

        valDeque.pop();
        while (!valDeque.isEmpty()) {
            while (valDeque.get(valDeque.size() - 2) == 0) {
                op.add(valDeque.size() - 1);
                valDeque.pop();
                valDeque.pop();
            }
            if (valDeque.size() >= 2 && valDeque.get(valDeque.size() - 2) == 1) {
                op.add(valDeque.size());
                valDeque.pop();
                valDeque.pop();
            }
        }

        out.println(op.size());
        for (int i = 0; i < op.size(); i++) {
            out.println(op.get(i));
        }
    }

    public void trace(int[] vals, int i, int j, int k) {
        if (j == i) {
            vals[j] = 1;
            return;
        }
        if (k == 2) {
            vals[j] = 1;
            trace(vals, i, j - 1, 1);
            return;
        }
        if (k == 1) {
            if (dp(i, j - 1, 0) == dp(i, j, k)) {
                trace(vals, i, j - 1, 0);
            } else {
                trace(vals, i, j - 1, 2);
            }
            return;
        }
        trace(vals, i, j - 1, 1);
    }

    public long dp(int i, int j, int k) {
        if (dp[i][j][k] == -1) {
            if (i == j) {
                dp[i][j][k] = k == 2 ? a[i] : -lInf;
                return dp[i][j][k];
            }
            if (k == 2) {
                dp[i][j][k] = a[j] + dp(i, j - 1, 1);
            } else if (k == 1) {
                dp[i][j][k] = Math.max(dp(i, j - 1, 0), dp(i, j - 1, 2));
            } else {
                dp[i][j][k] = dp(i, j - 1, 1);
            }
        }
        return dp[i][j][k];
    }
}
