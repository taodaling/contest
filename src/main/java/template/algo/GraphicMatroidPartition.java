package template.algo;

import template.datastructure.DSU;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Provide faster partition for graphic matroid.
 * O(k^2VE), k is the number of spanning tree ask for
 */
public class GraphicMatroidPartition {
    IntegerMultiWayStack[] trees;
    int[] a;
    int[] b;
    int[][] depth;
    int[][] p;
    int[][] roof;
    DSUExt[] dsu;
    int[] type;
    int k;
    int n;

    private int other(int e, int x) {
        return a[e] == x ? b[e] : a[e];
    }

    private void dfs(int t, int root, int f) {
        p[t][root] = f;
        if (f == -1) {
            depth[t][root] = 0;
            roof[t][root] = root;
        } else {
            int pnode = other(f, root);
            depth[t][root] = depth[t][pnode] + 1;
            roof[t][root] = roof[t][pnode];
        }
        for (IntegerIterator iterator = trees[t].iterator(root); iterator.hasNext(); ) {
            int e = iterator.next();
            if (e == f) {
                continue;
            }
            int node = other(e, root);
            dfs(t, node, e);
        }
    }

    private void prepare() {
        for (int i = 0; i < k; i++) {
            trees[i].clear();
            dsu[i].init();
        }
        for (int i = 0; i < a.length; i++) {
            if (type[i] == -1) {
                continue;
            }
            int t = type[i];
            trees[t].addLast(a[i], i);
            trees[t].addLast(b[i], i);
        }
        SequenceUtils.deepFill(depth, -1);
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                if (depth[i][j] != -1) {
                    continue;
                }
                dfs(i, j, -1);
            }
            dsu[i].height = depth[i];
        }
    }

    private int getTop(int t, int i) {
        return dsu[t].top[dsu[t].find(i)];
    }

    int[][] buf;
    int[] bufTail;

    private boolean augment(int t, int e) {
        int x = getTop(t, a[e]);
        int y = getTop(t, b[e]);
        if (roof[t][x] != roof[t][y]) {
            type[e] = t;
            return true;
        }
        int begin = bufTail[t];
        while (x != y) {
            if (depth[t][x] < depth[t][y]) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            buf[t][bufTail[t]++] = p[t][x];
            dsu[t].merge(x, other(p[t][x], x));
            x = getTop(t, x);
        }
        int end = bufTail[t];
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < k; j++) {
                if (t == j) {
                    continue;
                }
                if (augment(j, buf[t][i])) {
                    bufTail[t] = begin;
                    type[e] = t;
                    return true;
                }
            }
        }
        bufTail[t] = begin;
        return false;
    }

    public int[] solve(int n, int k, int[] a, int[] b) {
        this.a = a;
        this.b = b;
        this.k = k;
        this.n = n;
        assert a.length == b.length;
        trees = new IntegerMultiWayStack[k];
        dsu = new DSUExt[k];
        depth = new int[k][n];
        p = new int[k][n];
        roof = new int[k][n];
        type = new int[a.length];
        buf = new int[k][n];
        bufTail = new int[k];
        Arrays.fill(type, -1);
        for (int i = 0; i < k; i++) {
            trees[i] = new IntegerMultiWayStack(n, n - 1);
            dsu[i] = new DSUExt(n);
        }

        //shuffle and make like better
        int[] perm = IntStream.range(0, a.length).toArray();
        DSU dsu = new DSU(n);
        for (int i = 0; i < k; i++) {
            Randomized.shuffle(perm);
            dsu.init();
            for (int index : perm) {
                if (type[index] != -1) {
                    continue;
                }
                if (dsu.find(a[index]) != dsu.find(b[index])) {
                    dsu.merge(a[index], b[index]);
                    type[index] = i;
                }
            }
        }

        prepare();
        for (int i = 0; i < a.length; i++) {
            if (type[i] != -1) {
                continue;
            }
            for (int j = 0; j < k; j++) {
                if (augment(j, i)) {
                    prepare();
                    break;
                }
            }
        }

        return type;
    }

    private static class DSUExt extends DSU {
        private int[] top;
        int[] height;

        public DSUExt(int n) {
            super(n);
            top = new int[n];
        }

        @Override
        public void init(int n) {
            super.init(n);
            for (int i = 0; i < n; i++) {
                top[i] = i;
            }
        }

        @Override
        protected void preMerge(int a, int b) {
            super.preMerge(a, b);
            if (height[top[a]] > height[top[b]]) {
                top[a] = top[b];
            }
        }
    }
}


