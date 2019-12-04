package on2019_12.on2019_12_04_Codeforces_Round__547__Div__3_.G___Privatization_of_Roads_in_Treeland0;





import template.algo.IntBinarySearch;
import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaskG {
    MultiWayStack<Edge> edges;
    int[] cnt;
    int[] size;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        edges = new MultiWayStack<>(n + 1, n * 2);
        Edge[] es = new Edge[n];
        cnt = new int[n + 1];
        size = new int[n + 1];
        for (int i = 1; i < n; i++) {
            Edge e = new Edge();
            es[i] = e;
            e.a = in.readInt();
            e.b = in.readInt();
            edges.addFirst(e.a, e);
            edges.addFirst(e.b, e);
            size[e.a]++;
            size[e.b]++;
        }
        IntBinarySearch bs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                dfs(1, null, mid);
                return cnt[1] <= k;
            }
        };
        int ans = bs.binarySearch(1, n);
        bs.check(ans);
        out.println(ans);
        paint(1, null, ans);
        for (int i = 1; i < n; i++) {
            out.append(es[i].color + 1).append(' ');
        }
    }

    public void paint(int root, Edge p, int c) {
        int color = 0;
        if (p != null) {
            color = p.color + 1;
        }
        for (Edge e : edges.queue(root)) {
            if (e == p) {
                continue;
            }
            int node = e.other(root);
            e.color = color % c;
            color++;
            paint(node, e, c);
        }
    }

    public void dfs(int root, Edge p, int c) {
        cnt[root] = 0;
        if (size[root] > c) {
            cnt[root]++;
        }
        for (Edge e : edges.queue(root)) {
            if (e == p) {
                continue;
            }
            int node = e.other(root);
            dfs(node, e, c);
            cnt[root] += cnt[node];
        }
    }
}

class Edge {
    int a;
    int b;

    int other(int x) {
        return a ^ b ^ x;
    }

    int color;
}
