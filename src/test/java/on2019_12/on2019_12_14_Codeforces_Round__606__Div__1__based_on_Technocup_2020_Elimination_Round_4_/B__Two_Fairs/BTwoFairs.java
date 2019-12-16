package on2019_12.on2019_12_14_Codeforces_Round__606__Div__1__based_on_Technocup_2020_Elimination_Round_4_.B__Two_Fairs;



import template.datastructure.IntIterator;
import template.datastructure.MultiWayIntStack;
import template.io.FastInput;
import template.io.FastOutput;

import java.lang.reflect.Array;
import java.util.Arrays;

public class BTwoFairs {

    MultiWayIntStack edges;
    boolean[] visited;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int a = in.readInt() - 1;
        int b = in.readInt() - 1;
        edges = new MultiWayIntStack(n, m * 2);
        int[] aCnt = new int[n];
        int[] bCnt = new int[n];
        visited = new boolean[n];
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            edges.addLast(u, v);
            edges.addLast(v, u);
        }

        Arrays.fill(visited, false);
        dfs(a, aCnt, b);
        Arrays.fill(visited, false);
        dfs(b, bCnt, a);

        int aTotal = 0;
        int bTotal = 0;
        for (int i = 0; i < n; i++) {
            if (i == a || i == b) {
                continue;
            }
            if (aCnt[i] == 1 && bCnt[i] == 0) {
                aTotal++;
            }
            if (bCnt[i] == 1 && aCnt[i] == 0) {
                bTotal++;
            }
        }

        long ans = (long) aTotal * bTotal;
        out.println(ans);
    }

    public void dfs(int root, int[] cnt, int forbiden) {
        if (visited[root] || root == forbiden) {
            return;
        }
        visited[root] = true;
        cnt[root]++;
        for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            dfs(node, cnt, forbiden);
        }
    }
}