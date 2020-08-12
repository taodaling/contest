package contest;

import template.datastructure.PermutationNode;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class GoodSubsequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        x = in.readInt();

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = in.readInt() - 1;
        }

        PermutationNode root = PermutationNode.build(perm);
        dfs(root);

        out.println(ans * 2 % mod);
    }

    int x;
    long ans;
    long mod = 998244353;

    public void dfs(PermutationNode root) {
        if (!root.adj.isEmpty() && root.join) {
            int n = root.adj.size();
            int r = 0;

            int pre = 0;
            int size = root.adj.get(0).length();
            for (int i = 0; i < n; i++) {
                pre += root.adj.get(i).length();
                while (r < n && size <= pre + x) {
                    size += root.adj.get(r).length();
                    r++;
                }
                if (r < n) {
                    ans += (n - r);
                }
            }

            ans %= mod;
        }

        for (PermutationNode node : root.adj) {
            dfs(node);
        }
    }
}
