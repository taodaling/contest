package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.BitSet;
import template.utils.SequenceUtils;

public class BTheUnbearableLightnessOfWeights {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for (int x : a) {
            limit += x;
        }
        dp = new BitSet[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = new BitSet(limit + 1);
        }
        int ans = 0;
        int[] cnts = new int[101];
        for (int x : a) {
            cnts[x]++;
        }
        for (int i = 0; i <= 100; i++) {
            if (cnts[i] == 0) {
                continue;
            }
            comp(a, i);
            for (int j = 1; j <= cnts[i]; j++) {
                if (dp[j].get(i * j)) {
                    break;
                }
                ans = Math.max(ans, j);
            }
        }

        out.println(ans);
    }

    int n;
    int limit;
    BitSet[] dp;


    public void comp(int[] val, int forbidden) {
        for (int i = 0; i <= n; i++) {
            dp[i].fill(false);
        }
        BitSet tmp = new BitSet(limit + 1);
        dp[0].set(0);
        int cnt = 0;
        for (int x : val) {
            if (x == forbidden) {
                continue;
            }
            cnt++;
            for (int i = cnt; i >= 1; i--) {
                tmp.copy(dp[i - 1]);
                tmp.rightShift(x);
                dp[i].or(tmp);
            }
        }
    }
}
