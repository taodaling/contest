package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

public class FSpeedDial {
    int k;
    int inf = (int) 1e9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        Trie trie = new Trie();
        for (int i = 0; i < n; i++) {
            char[] s = in.readString().toCharArray();
            build(trie, s, 0, in.readInt());
        }

        int[] dp = dfsTrie(trie);
        int ans = SortUtils.minOf(dp, 0, k);
        out.println(ans);
    }

    public int[] dfsTrie(Trie root) {
        for (Trie node : root.next) {
            if (node == null) {
                continue;
            }
            dfs(node);
        }
        int[] dp2 = new int[k + 1];
        SequenceUtils.deepFill(dp2, inf);
        dp2[0] = 0;
        for (Trie node : root.next) {
            if (node == null) {
                continue;
            }
            for (int j = k; j >= 0; j--) {
                int val = inf;
                for (int t = 0; t <= j; t++) {
                    val = Math.min(dp2[j - t] + node.dp[root.depth][t], val);
                }
                dp2[j] = val;
            }
        }

        return dp2;
    }

    public void dfs(Trie root) {
        for (Trie node : root.next) {
            if (node == null) {
                continue;
            }
            dfs(node);
        }
        int[][] dp1 = new int[root.depth][k + 1];
        int[] dp2 = new int[k + 1];
        SequenceUtils.deepFill(dp1, inf);
        SequenceUtils.deepFill(dp2, inf);
        dp2[1] = 0;
        for (int i = 0; i < root.depth; i++) {
            dp1[i][0] = root.w * (root.depth - i);
        }
        for (Trie node : root.next) {
            if (node == null) {
                continue;
            }
            for (int i = 0; i < root.depth; i++) {
                for (int j = k; j >= 0; j--) {
                    int val = inf;
                    for (int t = 0; t <= j; t++) {
                        val = Math.min(dp1[i][j - t] + node.dp[i][t], val);
                    }
                    dp1[i][j] = val;
                }
            }

            for (int j = k; j >= 0; j--) {
                int val = inf;
                for (int t = 0; t <= j; t++) {
                    val = Math.min(dp2[j - t] + node.dp[root.depth][t], val);
                }
                dp2[j] = val;
            }
        }

        for (int i = 0; i < root.depth; i++) {
            for (int j = 0; j <= k; j++) {
                dp1[i][j] = Math.min(dp1[i][j], dp2[j]);
            }
        }

        root.dp = dp1;
    }

    public void build(Trie root, char[] s, int i, int w) {
        if (i == s.length) {
            root.w += w;
            return;
        }
        build(root.get(s[i] - '0'), s, i + 1, w);
    }

}

class Trie {
    int w;
    int depth;
    Trie[] next = new Trie[10];
    int[][] dp;

    public Trie get(int i) {
        if (next[i] == null) {
            next[i] = new Trie();
            next[i].depth = depth + 1;
        }
        return next[i];
    }
}
