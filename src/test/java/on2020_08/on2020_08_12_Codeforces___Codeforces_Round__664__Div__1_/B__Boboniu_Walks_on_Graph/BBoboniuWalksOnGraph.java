package on2020_08.on2020_08_12_Codeforces___Codeforces_Round__664__Div__1_.B__Boboniu_Walks_on_Graph;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BBoboniuWalksOnGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        Edge[] edges = new Edge[m];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            e.to = v;
            e.w = in.readInt();
            u.adj.add(e);
        }

        groups = new int[k + 1][];
        int order = 0;
        for (int i = 1; i <= k; i++) {
            groups[i] = new int[i];
            for (int j = 0; j < i; j++) {
                groups[i][j] = order++;
            }
        }

        disallow = new boolean[order][order];
        invalid = new boolean[order];

        long[] added = new long[n];
        for (Node node : nodes) {
            node.adj.sort(Edge.sortByW);
            int size = node.adj.size();
            for (int j = 0; j < size; j++) {
                int id = groups[size][j];
                int to = node.adj.get(j).to.id;
                if (Bits.get(added[to], id) == 1) {
                    invalid[id] = true;
                }
                added[to] = Bits.set(added[to], id);
                for (int t = 0; t < order; t++) {
                    if (Bits.get(added[to], t) == 1) {
                        disallow[t][id] = disallow[id][t] = true;
                    }
                }
            }
        }

        int ans = dfs(1, new int[k + 1]);
        out.println(ans);
    }

    public int dfs(int i, int[] picked) {
        if (i == groups.length) {
            return 1;
        }
        int ans = 0;
        for (int j = 0; j < i; j++) {
            int id = groups[i][j];
            if (invalid[id]) {
                continue;
            }

            boolean allow = true;
            for (int k = 1; k < i; k++) {
                if (disallow[picked[k]][id]) {
                    allow = false;
                    break;
                }
            }
            if(allow){
                picked[i] = id;
                ans += dfs(i + 1, picked);
            }
        }
        return ans;
    }

    boolean[] invalid;
    boolean[][] disallow;
    int[][] groups;
}

class Edge {
    Node to;
    int w;

    static Comparator<Edge> sortByW = (a, b) -> Integer.compare(a.w, b.w);
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int id;
}
