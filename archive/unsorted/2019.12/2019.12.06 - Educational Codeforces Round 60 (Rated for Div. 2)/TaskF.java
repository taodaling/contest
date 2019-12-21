package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.polynomial.FastWalshHadamardTransform;
import template.utils.SequenceUtils;

public class TaskF {
    BitOperator bo = new BitOperator();
    int p;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        p = in.readInt();
        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readChar() - 'a';
        }
        int[] mask = new int[p];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                mask[i] = bo.setBit(mask[i], j, in.readInt() == 0);
            }
        }
        IntegerList[] next = new IntegerList[n];
        IntegerList[] prev = new IntegerList[n];

        prev[0] = new IntegerList();
        for (int i = 1; i < n; i++) {
            prev[i] = new IntegerList(prev[i - 1].size() + 1);
            prev[i].addAll(prev[i - 1]);
            int index = prev[i].indexOf(s[i - 1]);
            if (index >= 0) {
                prev[i].remove(index);
            }
            prev[i].add(s[i - 1]);
        }
        next[n - 1] = new IntegerList();
        for (int i = n - 2; i >= 0; i--) {
            next[i] = new IntegerList(next[i + 1].size() + 1);
            next[i].addAll(next[i + 1]);
            int index = next[i].indexOf(s[i + 1]);
            if (index >= 0) {
                next[i].remove(index);
            }
            next[i].add(s[i + 1]);
        }

        cnts = new int[1 << p];
        for (int i = 0; i < n; i++) {
            int v = s[i];
            int m = 0;
            for (int j = prev[i].size() - 1; j >= 0; j--) {
                int u = prev[i].get(j);
                if (bo.bitAt(mask[v], u) == 1) {
                    cnts[m]++;
                    cnts[m | (1 << u)]--;
                    cnts[m | (1 << v)]--;
                    cnts[m | (1 << u) | (1 << v)]++;
                }
                m = bo.setBit(m, u, true);
            }
            m = 0;
            for (int j = next[i].size() - 1; j >= 0; j--) {
                int u = next[i].get(j);
                if (bo.bitAt(mask[v], u) == 1) {
                    cnts[m]++;
                    cnts[m | (1 << u)]--;
                    cnts[m | (1 << v)]--;
                    cnts[m | (1 << u) | (1 << v)]++;
                }
                m = bo.setBit(m, u, true);
            }
        }

        FastWalshHadamardTransform.orFWT(cnts, 0, cnts.length - 1);
        charCnt = new int[p];
        for (int i = 0; i < n; i++) {
            charCnt[s[i]]++;
        }
        dp = new int[1 << p];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(0);
        out.println(ans);
    }

    int[] charCnt;
    int[] dp;
    int[] cnts;

    public int dp(int s) {
        if (dp[s] == -1) {
            dp[s] = 0;
            if (cnts[s] > 0) {
                return dp[s] = (int) 1e8;
            }
            for (int i = 0; i < p; i++) {
                if (bo.bitAt(s, i) == 0) {
                    dp[s] += charCnt[i];
                }
            }
            for (int i = 0; i < p; i++) {
                if (bo.bitAt(s, i) == 0) {
                    dp[s] = Math.min(dp[s], dp(bo.setBit(s, i, true)));
                }
            }
        }
        return dp[s];
    }
}
