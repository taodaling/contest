package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.D__Lost_Tree;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DLostTree {
    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        n = in.ri();
        int[] status = query(0);
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            cnt[status[i] & 1]++;
        }
        if (cnt[0] > cnt[1]) {
            int pick = 0;
            for (int i = 0; i < n; i++) {
                if (status[i] % 2 == 1) {
                    pick = i;
                    break;
                }
            }
            dfs(pick, -1, new int[n], null);
        } else {
            dfs(0, -1, new int[n], status);
        }

        out.println("!");
        for (int[] e : edges) {
            out.append(e[0] + 1).append(' ').append(e[1] + 1).println();
        }
        out.flush();
    }

    int n;
    List<int[]> edges = new ArrayList<>();

    void addEdge(int u, int v) {
        edges.add(new int[]{u, v});
    }

    void dfs(int root, int p, int[] pStatus, int[] qs) {
        int[] ans = qs != null ? qs : query(root);
        for (int i = 0; i < n; i++) {
            if (ans[i] == 1) {
                addEdge(root, i);
            } else if (ans[i] == 2) {
                if (i == p || pStatus[i] == 2) {
                } else {
                    dfs(i, root, ans, null);
                }
            }
        }
    }

    public int[] query(int node) {
        out.printf("? %d", node + 1).println().flush();
        return in.ri(n);
    }
}
