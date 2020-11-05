package on2020_11.on2020_11_03_Codeforces___Codeforces_Round__345__Div__1_.D__Zip_line;



import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DZipLine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] h = new int[n];
        in.populate(h);
        Query[] qs = new Query[m];
        MultiWayStack<Query> stack = new MultiWayStack<>(n, m);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            qs[i] = new Query();
            qs[i].x = in.readInt();
            stack.addLast(a, qs[i]);
        }

        //backward
        int inf = (int) 2e9;
        int[] lis = new int[n];

        int[] dp = new int[n];
        Arrays.fill(dp, inf);
        for (int i = 0; i < n; i++) {
            int wpos = SequenceUtils.upperBound(dp, h[i], 0, n - 1);
            for (Query q : stack.getStack(i)) {
                q.left = SequenceUtils.upperBound(dp, q.x, 0, n - 1);
            }
            dp[wpos] = h[i];
            lis[i] = wpos + 1;
        }
        int best = 0;
        while (best + 1 < n && dp[best + 1] < inf) {
            best++;
        }
        best++;

        Arrays.fill(dp, -1);
        for (int i = n - 1; i >= 0; i--) {
            int wpos = SequenceUtils.lowerBound(dp, h[i], 0, n - 1);
            for (Query q : stack.getStack(i)) {
                q.right = n - 1 - SequenceUtils.lowerBound(dp, q.x, 0, n - 1);
            }
            dp[wpos] = h[i];
        }

        boolean[] added = new boolean[n];
        int[] active = new int[n + 2];
        int[] highest = new int[n + 2];
        for (int i = n - 1; i >= 0; i--) {
            if (lis[i] == best || highest[lis[i] + 1] > h[i]) {
                active[lis[i]]++;
                highest[lis[i]] = Math.max(highest[lis[i]], h[i]);
                added[i] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            boolean necessary = added[i] && active[lis[i]] == 1;
            int cand = necessary ? best - 1 : best;

            for (Query q : stack.getStack(i)) {
                q.ans = Math.max(cand, q.left + q.right + 1);
            }
        }

        for (Query q : qs) {
            out.println(q.ans);
        }
    }
}

class Query {
    int x;
    int ans;

    int left;
    int right;
}