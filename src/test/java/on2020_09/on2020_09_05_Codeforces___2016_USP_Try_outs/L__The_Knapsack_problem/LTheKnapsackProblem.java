package on2020_09.on2020_09_05_Codeforces___2016_USP_Try_outs.L__The_Knapsack_problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class LTheKnapsackProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s = in.readInt();
        items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].w = in.readInt();
            items[i].c = in.readLong();
        }
        bf = new long[1000 + 1];
        for (int i = 1; i <= 1000; i++) {
            for (Item item : items) {
                if (item.w <= i) {
                    bf[i] = Math.max(bf[i], bf[i - item.w] + item.c);
                }
            }
        }
        dp = new long[32][3000];
        SequenceUtils.deepFill(dp, -1L);
        start = new int[32];
        for (int i = 0; i < 32; i++) {
            start[i] = (int) (s / (1L << i) - 1000);
        }
        long ans = dp(s, 0);
        out.println(ans);

    }

    long[] bf;
    Item[] items;
    long[][] dp;
    int[] start;
    int W = 500;

    public long dp(int n, int level) {
        if (n <= 1000) {
            return bf[n];
        }
        int offset = n - start[level];
        if (dp[level][offset] == -1) {
            long ans = 0;
            for (Item item : items) {
                if (item.w <= n) {
                    ans = Math.max(ans, item.c);
                }
            }
            //or split
            for (int l = Math.max(1, n / 2 - W); ; l++) {
                int r = n - l;
                if (r < l) {
                    break;
                }
                ans = Math.max(ans, dp(l, level + 1) + dp(r, level + 1));
            }

            dp[level][offset] = ans;
        }
        return dp[level][offset];
    }
}

class Item {
    int w;
    long c;

    @Override
    public String toString() {
        return String.format("(%d, %d)", w, c);
    }
}

