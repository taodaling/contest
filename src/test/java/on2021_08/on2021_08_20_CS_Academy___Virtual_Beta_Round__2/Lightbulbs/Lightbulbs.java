package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Lightbulbs;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class Lightbulbs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        SequenceUtils.reverse(s);
        int n = s.length;
        long[] cost = new long[n];
        cost[0] = 1;
        for (int i = 1; i < n; i++) {
            cost[i] = cost[i - 1] + 1 + cost[i - 1];
        }
        long inf = (long) 2e18;
        long[] prev = new long[2];
        long[] next = new long[2];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int offset = 0; offset < n; offset++) {
            int v = s[offset] - '0';
            Arrays.fill(next, inf);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    int change = j ^ v;
                    long take = 0;
                    if (change == 1) {
                        take = cost[offset];
                        if (offset > 0 && i == 1) {
                            take -= cost[offset - 1];
                        }
                    } else {
                        if (offset > 0 && i == 1) {
                            take += cost[offset - 1];
                        }
                    }
                    next[j] = Math.min(next[j], prev[i] + take);
                }
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = prev[0];
        out.println(ans);
    }
}
