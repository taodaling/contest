package template.graph;

import template.math.CachedLog2;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.MultiWayIntegerStack;

// Answering LCA queries in O(1) with O(n) preprocessing
public class LcaOnTree {
    int[] parent;
    int[] preOrder;
    int[] i;
    int[] head;
    int[] a;
    int time;

    void dfs1(MultiWayIntegerStack tree, int u, int p) {
        parent[u] = p;
        i[u] = preOrder[u] = time++;
        for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
            int v = iterator.next();
            if (v == p) continue;
            dfs1(tree, v, u);
            if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                i[u] = i[v];
            }
        }
        head[i[u]] = u;
    }

    void dfs2(MultiWayIntegerStack tree, int u, int p, int up) {
        a[u] = up | Integer.lowestOneBit(i[u]);
        for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
            int v = iterator.next();
            if (v == p) continue;
            dfs2(tree, v, u, a[u]);
        }
    }

    public LcaOnTree(MultiWayIntegerStack tree, int root) {
        int n = tree.stackNumber();
        preOrder = new int[n];
        i = new int[n];
        head = new int[n];
        a = new int[n];
        parent = new int[n];

        dfs1(tree, root, -1);
        dfs2(tree, root, -1, 0);
    }

    private int enterIntoStrip(int x, int hz) {
        if (Integer.lowestOneBit(i[x]) == hz)
            return x;
        int hw = 1 << CachedLog2.floorLog(a[x] & (hz - 1));
        return parent[head[i[x] & -hw | hw]];
    }

    public int lca(int x, int y) {
        int hb = i[x] == i[y] ? Integer.lowestOneBit(i[x]) : (1 << CachedLog2.floorLog(i[x] ^ i[y]));
        int hz = Integer.lowestOneBit(a[x] & a[y] & -hb);
        int ex = enterIntoStrip(x, hz);
        int ey = enterIntoStrip(y, hz);
        return preOrder[ex] < preOrder[ey] ? ex : ey;
    }
}
