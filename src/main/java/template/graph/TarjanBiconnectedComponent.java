package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tarjan 点双算法
 */
public class TarjanBiconnectedComponent {
    public List<int[]> vcc;
    IntegerArrayList vertex;
    IntegerDeque dq;
    List<? extends UndirectedEdge>[] g;
    int[] dfn;
    int[] low;
    int time;
    public int[] occur;

    public TarjanBiconnectedComponent(int n) {
        vcc = new ArrayList<>(n);
        vertex = new IntegerArrayList(n);
        dq = new IntegerDequeImpl(n);
        dfn = new int[n];
        low = new int[n];
        occur = new int[n];
    }

    public void init(List<? extends UndirectedEdge>[] g) {
        int n = g.length;
        this.g = g;
        Arrays.fill(dfn, 0, n, 0);
        Arrays.fill(occur, 0, n, 0);
        vcc.clear();
        for (int i = 0; i < n; i++) {
            if (dfn[i] == 0) {
                dq.clear();
                dfs(i, null);
            }
        }
        for (int[] vc : vcc) {
            for (int v : vc) {
                occur[v]++;
            }
        }
    }

    public boolean isCut(int v) {
        return occur[v] > 1;
    }

    private void dfs(int root, UndirectedEdge p) {
        dfn[root] = low[root] = ++time;
        dq.addLast(root);
        int diff = 0;
        for (UndirectedEdge e : g[root]) {
            if (e.rev == p || e.to == root) {
                continue;
            }
            diff++;
            if (dfn[e.to] == 0) {
                dfs(e.to, e);
                low[root] = Math.min(low[root], low[e.to]);
                if (low[e.to] >= dfn[root]) {
                    //vcc
                    vertex.clear();
                    vertex.add(root);
                    while (true) {
                        int tail = dq.removeLast();
                        vertex.add(tail);
                        if (tail == e.to) {
                            break;
                        }
                    }
                    vcc.add(vertex.toArray());
                }
            } else {
                low[root] = Math.min(low[root], dfn[e.to]);
            }
        }
        if (diff == 0 && p == null) {
            vcc.add(new int[]{root});
        }
    }
}
