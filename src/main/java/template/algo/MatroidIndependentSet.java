package template.algo;

import template.binary.Bits;
import template.datastructure.DSU;
import template.datastructure.LinearBasis;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.Arrays;

public interface MatroidIndependentSet {
    /**
     * adj[i][j] = true only when added[i] = true and added[j] = false and it's possible to replace i by j without loss of
     * independence
     */
    void computeAdj(boolean[] added, boolean[][] adj);

    /**
     * extendable[i] means current independent set still independent after adding i
     *
     * @param added
     * @param extendable
     */
    void extend(boolean[] added, boolean[] extendable);

    void prepare(boolean[] added);

    /**
     * The set is independent only when for any i satisfy T[i] <= cap[i],
     * while T[i] is the number of elements with type i in set
     *
     * @param type
     * @param cap
     * @return
     */
    static MatroidIndependentSet ofColorContainers(int[] type, int[] cap) {
        int[] size = new int[cap.length];
        return new MatroidIndependentSet() {
            /**
             * O(rn)
             * @param added
             * @param adj
             */
            @Override
            public void computeAdj(boolean[] added, boolean[][] adj) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        continue;
                    }
                    for (int j = 0; j < added.length; j++) {
                        if (added[j]) {
                            continue;
                        }
                        if (size[type[j]] < cap[type[j]] ||
                                type[i] == type[j]) {
                            adj[i][j] = true;
                        }
                    }
                }
            }

            /**
             * O(n)
             * @param added
             * @param extendable
             */
            @Override
            public void extend(boolean[] added, boolean[] extendable) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i] && size[type[i]] < cap[type[i]]) {
                        extendable[i] = true;
                    }
                }
            }

            @Override
            public void prepare(boolean[] added) {
                Arrays.fill(size, 0);
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        size[type[i]]++;
                    }
                }
            }
        };
    }

    /**
     * The set is independent only when no duplicate color in set
     *
     * @param colors
     * @return
     */
    static MatroidIndependentSet ofColor(int[] colors) {
        boolean[] exist = new boolean[colors.length];
        return new MatroidIndependentSet() {
            /**
             * O(rn)
             */
            @Override
            public void computeAdj(boolean[] added, boolean[][] adj) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        continue;
                    }
                    for (int j = 0; j < added.length; j++) {
                        if (added[j]) {
                            continue;
                        }
                        adj[i][j] = !exist[colors[j]] || colors[i] == colors[j];
                    }
                }
            }

            /**
             * O(n)
             */
            @Override
            public void extend(boolean[] added, boolean[] extendable) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        extendable[i] = !exist[colors[i]];
                    }
                }
            }

            @Override
            public void prepare(boolean[] added) {
                Arrays.fill(exist, false);
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        exist[colors[i]] = true;
                    }
                }
            }
        };
    }

    /**
     * The set is independent only when all vector is linear independent
     *
     * @param nums
     * @return
     */
    static MatroidIndependentSet ofLinearBasis(long[] nums) {
        LinearBasis lb = new LinearBasis();
        int[] invIndex = new int[64];
        return new MatroidIndependentSet() {
            /**
             * O(64n)
             * @param added
             * @param adj
             */
            @Override
            public void computeAdj(boolean[] added, boolean[][] adj) {
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        continue;
                    }
                    if (!lb.contain(nums[i])) {
                        for (int j = 0; j < 64; j++) {
                            if (invIndex[j] != -1) {
                                adj[invIndex[j]][i] = true;
                            }
                        }
                    } else {
                        long bits = lb.representationOriginal(nums[i]);
                        for (int j = 0; j < 64; j++) {
                            if (Bits.get(bits, j) == 1 && invIndex[j] != -1) {
                                adj[invIndex[j]][i] = true;
                            }
                        }
                    }
                }
            }

            /**
             * O(64n)
             * @param added
             * @param extendable
             */
            @Override
            public void extend(boolean[] added, boolean[] extendable) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        extendable[i] = !lb.contain(nums[i]);
                    }
                }
            }

            @Override
            public void prepare(boolean[] added) {
                lb.clear();
                Arrays.fill(invIndex, -1);
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        int index = lb.add(nums[i]);
                        assert index >= 0;
                        invIndex[index] = i;
                    }
                }
            }
        };
    }

    /**
     * <pre>ei = (edges[0][i], edges[1][i]);</pre>
     * <pre>The set is independent only when no circle formed by selected edges</pre>
     */
    static MatroidIndependentSet ofSpanningTree(int n, int[][] edges) {
        return new MatroidIndependentSet() {
            DSU dsu = new DSU(n);
            IntegerMultiWayStack g = new IntegerMultiWayStack(n, edges[0].length);
            IntegerArrayList inset = new IntegerArrayList(edges[0].length);
            int[] p = new int[n];
            int[] depth = new int[n];

            public void dfs(int root, int fa, int d) {
                p[root] = fa;
                depth[root] = d;
                for (IntegerIterator iterator = g.iterator(root); iterator.hasNext(); ) {
                    int e = iterator.next();
                    if (e == fa) {
                        continue;
                    }
                    dfs(opponent(e, root), e, d + 1);
                }
            }

            public int opponent(int i, int root) {
                return edges[0][i] == root ? edges[1][i] : edges[0][i];
            }

            /**
             * O(rn)
             */
            @Override
            public void computeAdj(boolean[] added, boolean[][] adj) {
                int[] insetData = inset.getData();
                int m = inset.size();
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        continue;
                    }
                    if (dsu.find(edges[0][i]) != dsu.find(edges[1][i])) {
                        for (int j = 0; j < m; j++) {
                            adj[insetData[j]][i] = true;
                        }
                    } else {
                        int a = edges[0][i];
                        int b = edges[1][i];
                        while (a != b) {
                            if (depth[a] < depth[b]) {
                                int tmp = a;
                                a = b;
                                b = tmp;
                            }
                            adj[p[a]][i] = true;
                            a = opponent(p[a], a);
                        }
                    }
                }
            }


            /**
             * O(n)
             */
            @Override
            public void extend(boolean[] added, boolean[] extendable) {
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        extendable[i] = dsu.find(edges[0][i]) != dsu.find(edges[1][i]);
                    }
                }
            }

            /**
             * O(r+n)
             */
            @Override
            public void prepare(boolean[] added) {
                g.clear();
                dsu.init();
                inset.clear();
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        dsu.merge(edges[0][i], edges[1][i]);
                        g.addLast(edges[0][i], i);
                        g.addLast(edges[1][i], i);
                        inset.add(i);
                    }
                }
                Arrays.fill(p, -1);
                for (int i = 0; i < n; i++) {
                    if (p[i] == -1) {
                        dfs(i, -1, 0);
                    }
                }
            }
        };
    }

    /**
     * 一个边集是独立的当且仅当删除这些边不会使得图不联通
     */
    public static MatroidIndependentSet ofDeletionOnGraphRetainConnected(int n, int[][] edges) {
        return new MatroidIndependentSet() {
            int[] left = new int[n];
            int[] right = new int[n];
            int[] dfn = new int[n];
            int[] set = new int[n];
            int[] low = new int[n];
            boolean[] instk = new boolean[n];
            IntegerDequeImpl dq = new IntegerDequeImpl(n);
            IntegerMultiWayStack g = new IntegerMultiWayStack(n, edges.length);
            int dfnOrder = 0;
            int idOrder = 0;
            IntegerArrayList inset = new IntegerArrayList(edges[0].length);

            /**
             * O(rn)
             */
            @Override
            public void computeAdj(boolean[] added, boolean[][] adj) {
                int[] insetData = inset.getData();
                int m = inset.size();
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        continue;
                    }
                    int a = set[edges[0][i]];
                    int b = set[edges[1][i]];
                    if (set[a] == set[b]) {
                        for (int j = 0; j < m; j++) {
                            adj[insetData[j]][i] = true;
                        }
                    } else {
                        int l = Math.max(left[a], left[b]);
                        int r = Math.min(right[a], right[b]);
                        for (int j = 0; j < m; j++) {
                            if (include(edges[0][insetData[j]], l, r) != include(edges[1][insetData[j]], l, r)) {
                                adj[insetData[j]][i] = true;
                            }
                        }
                    }
                }
            }

            public boolean include(int i, int l, int r) {
                return left[set[i]] >= l && right[set[i]] <= r;
            }

            /**
             * O(n)
             * @param added
             * @param extendable
             */
            @Override
            public void extend(boolean[] added, boolean[] extendable) {
                for (int i = 0; i < added.length; i++) {
                    if (added[i]) {
                        continue;
                    }
                    extendable[i] = set[edges[0][i]] == set[edges[1][i]];
                }
            }

            public int opponent(int i, int root) {
                return edges[0][i] == root ? edges[1][i] : edges[0][i];
            }

            private void tarjan(int root, int p) {
                if (dfn[root] != -1) {
                    return;
                }
                low[root] = dfn[root] = dfnOrder++;
                dq.addLast(root);
                instk[root] = true;
                for (IntegerIterator iterator = g.iterator(root); iterator.hasNext(); ) {
                    int e = iterator.next();
                    if (e == p) {
                        continue;
                    }
                    int node = opponent(e, root);
                    tarjan(node, e);
                    if (instk[node]) {
                        low[root] = Math.min(low[root], low[node]);
                    }
                }
                if (dfn[root] == low[root]) {
                    while (true) {
                        int tail = dq.removeLast();
                        set[tail] = root;
                        instk[tail] = false;
                        if (tail == root) {
                            break;
                        }
                    }
                }
            }

            private void allocId(int root, int p) {
                if (instk[root]) {
                    return;
                }
                instk[root] = true;

                int l, r;
                l = r = idOrder++;
                for (IntegerIterator iterator = g.iterator(root); iterator.hasNext(); ) {
                    int e = iterator.next();
                    if (e == p) {
                        continue;
                    }
                    int node = opponent(e, root);
                    allocId(node, e);
                    l = Math.min(left[set[node]], l);
                    r = Math.max(right[set[node]], r);
                }
                left[set[root]] = Math.min(left[set[root]], l);
                right[set[root]] = Math.max(right[set[root]], r);
            }

            /**
             * O(rn)
             * @param added
             */
            @Override
            public void prepare(boolean[] added) {
                dfnOrder = idOrder = 0;
                g.clear();
                inset.clear();
                for (int i = 0; i < added.length; i++) {
                    if (!added[i]) {
                        g.addLast(edges[0][i], i);
                        g.addLast(edges[1][i], i);
                    } else {
                        inset.add(i);
                    }
                }
                Arrays.fill(instk, false);
                Arrays.fill(dfn, -1);
                Arrays.fill(left, Integer.MAX_VALUE);
                Arrays.fill(right, Integer.MIN_VALUE);
                tarjan(0, -1);
                allocId(0, -1);
            }
        };
    }
}