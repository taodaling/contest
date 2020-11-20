package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class DynamicStringComplexity {
    int threshold = (int) 2e5;
    int[] p = new int[threshold];
    int[][] prevAncestor = new int[2][threshold];
    //int[] depth = new int[threshold];
    int[] s = new int[threshold];
    int[] period = new int[threshold];
    int[] prefix = new int[threshold];
    int n;

    void build(int i, int v) {
        int last = prevAncestor[v][i - 1];
        if (s[last + 1] == v) {
            last++;
        }
        s[i] = v;
        p[i] = last;
        // depth[i] = depth[last] + 1;
        prevAncestor[0][i] = prevAncestor[0][last];
        prevAncestor[1][i] = prevAncestor[1][last];
        prevAncestor[s[last + 1]][i] = last;
        period[i] = i - last;
        prefix[i] = prefix[p[i]] + (period[i] > period[p[i]] ? 1 : 0);
        sum += prefix[p[i]];
    }

    void destroy(int i) {
        s[i] = -1;
        sum -= prefix[p[i]];
    }


    long sum = 0;
    long[] best = new long[26];

    void dfs(int i) {
        best[i - 1] = Math.max(best[i - 1], sum);
//        if (i == 4 && sum == 6) {
//            StringBuilder b = new StringBuilder();
//            for (int j = n + 1; j <= n + 3; j++) {
//                b.append(s[j]);
//            }
//            debug.debug("b", b);
//        }
        if (i > 25) {
            return;
        }
        build(n + i, 0);
        dfs(i + 1);
        destroy(n + i);

        build(n + i, 1);
        dfs(i + 1);
        destroy(n + i);
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] data = new int[threshold];
        Arrays.fill(s, -1);
        n = in.readString(data, 1);
        for (int i = 1; i <= n; i++) {
            data[i] = data[i] == '.' ? 0 : 1;
        }
        for (int i = 1; i <= n; i++) {
            build(i, data[i]);
            debug.debug("sum", sum);
        }
        long now = sum;
        dfs(1);
        assert sum == now;
        for (int i = 1; i <= 25; i++) {
            out.println(best[i]);
        }
    }
}

