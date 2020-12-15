package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ExplodeEmAll {
    int[] defer;
    int ans;

    public void dfs(int i, int build, int set) {
        if (i == defer.length) {
            int cand = Math.max(Integer.bitCount(build), Integer.bitCount(set));
            ans = Math.min(cand, ans);
            return;
        }
        dfs(i + 1, build, set | defer[i]);
        dfs(i + 1, build | (1 << i), set);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        defer = new int[n];
        ans = n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (in.rc() == '*') {
                    defer[i] |= 1 << j;
                }
            }
        }
        dfs(0, 0, 0);
        out.println(ans);
    }
}
