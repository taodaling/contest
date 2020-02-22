package template.algo;

import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.Arrays;
import java.util.List;

public class MoOnTree {
    private List<UndirectedEdge>[] edges;
    private boolean[] odd;
    private int[] eulerTour;
    private int eulerTourTail = 0;
    private int[] begin;
    private int[] end;
    private LcaOnTree lcaOnTree;

    public MoOnTree(List<UndirectedEdge>[] edges) {
        this.edges = edges;
        odd = new boolean[edges.length];
        eulerTour = new int[edges.length * 2];
        begin = new int[edges.length];
        end = new int[edges.length];
        dfs(0, -1);
        lcaOnTree = new LcaOnTree(edges, 0);
    }

    private void dfs(int root, int p) {
        begin[root] = eulerTourTail;
        eulerTour[eulerTourTail++] = root;

        for (UndirectedEdge e : edges[root]) {
            int node = e.to;
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }

        end[root] = eulerTourTail;
        eulerTour[eulerTourTail++] = root;
    }

    public <T, Q extends Query> void handle(T[] data, Q[] queries, Handler<T, Q> handler) {
        handle(data, queries, handler, (int) Math.ceil(Math.sqrt(eulerTour.length)));
    }

    public <T, Q extends Query> void handle(T[] data, Q[] queries, Handler<T, Q> handler,
                                            int blockSize) {
        if (data.length == 0 || queries.length == 0) {
            return;
        }

        QueryWrapper<Q>[] wrappers = new QueryWrapper[queries.length];
        for (int i = 0; i < queries.length; i++) {
            Q q = queries[i];
            wrappers[i] = new QueryWrapper<Q>();
            wrappers[i].q = q;
            int u = q.getU();
            int v = q.getV();
            int ul = begin[u];
            int ur = end[u];
            int vl = begin[v];
            int vr = end[v];

            if (ur > vr) {
                int tmp = ul;
                ul = vl;
                vl = tmp;

                tmp = ur;
                ur = vr;
                vr = tmp;
            }

            if (ur < vl) {
                wrappers[i].l = ur;
                wrappers[i].r = vl;
                wrappers[i].extra = end[lcaOnTree.lca(u, v)];
            } else {
                wrappers[i].l = ur;
                wrappers[i].r = vr - 1;
                wrappers[i].extra = vr;
            }
        }

        Arrays.fill(odd, false);
        Arrays.sort(wrappers, (a, b) -> {
            int ans = a.l / blockSize - b.l / blockSize;
            if (ans == 0) {
                ans = a.r - b.r;
            }
            return ans;
        });

        int l = wrappers[0].l;
        int r = l - 1;
        for (QueryWrapper<Q> wrapper : wrappers) {
            int ll = wrapper.l;
            int rr = wrapper.r;
            while (l > ll) {
                l--;
                invert(eulerTour[l], data[eulerTour[l]], handler);
            }
            while (r < rr) {
                r++;
                invert(eulerTour[r], data[eulerTour[r]], handler);
            }
            while (l < ll) {
                invert(eulerTour[l], data[eulerTour[l]], handler);
                l++;
            }
            while (r > rr) {
                invert(eulerTour[r], data[eulerTour[r]], handler);
                r--;
            }
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
            handler.answer(wrapper.q);
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
        }
    }

    public <Q extends Query> void handle(int[] data, Q[] queries, IntHandler<Q> handler) {
        handle(data, queries, handler, (int) Math.ceil(Math.sqrt(eulerTour.length)));
    }

    public <Q extends Query> void handle(int[] data, Q[] queries, IntHandler<Q> handler,
                                         int blockSize) {
        if (data.length == 0 || queries.length == 0) {
            return;
        }

        QueryWrapper<Q>[] wrappers = new QueryWrapper[queries.length];
        for (int i = 0; i < queries.length; i++) {
            Q q = queries[i];
            wrappers[i] = new QueryWrapper<Q>();
            wrappers[i].q = q;
            int u = q.getU();
            int v = q.getV();
            int ul = begin[u];
            int ur = end[u];
            int vl = begin[v];
            int vr = end[v];

            if (ur > vr) {
                int tmp = ul;
                ul = vl;
                vl = tmp;

                tmp = ur;
                ur = vr;
                vr = tmp;
            }

            if (ur < vl) {
                wrappers[i].l = ur;
                wrappers[i].r = vl;
                wrappers[i].extra = end[lcaOnTree.lca(u, v)];
            } else {
                wrappers[i].l = ur;
                wrappers[i].r = vr - 1;
                wrappers[i].extra = vr;
            }
        }

        Arrays.fill(odd, false);
        Arrays.sort(wrappers, (a, b) -> {
            int ans = a.l / blockSize - b.l / blockSize;
            if (ans == 0) {
                ans = a.r - b.r;
            }
            return ans;
        });

        int l = wrappers[0].l;
        int r = l - 1;
        for (QueryWrapper<Q> wrapper : wrappers) {
            int ll = wrapper.l;
            int rr = wrapper.r;
            while (l > ll) {
                l--;
                invert(eulerTour[l], data[eulerTour[l]], handler);
            }
            while (r < rr) {
                r++;
                invert(eulerTour[r], data[eulerTour[r]], handler);
            }
            while (l < ll) {
                invert(eulerTour[l], data[eulerTour[l]], handler);
                l++;
            }
            while (r > rr) {
                invert(eulerTour[r], data[eulerTour[r]], handler);
                r--;
            }
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
            handler.answer(wrapper.q);
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
        }
    }

    private <T, Q extends Query> void invert(int i, T x, Handler<T, Q> handler) {
        odd[i] = !odd[i];
        if (odd[i]) {
            handler.add(i, x);
        } else {
            handler.remove(i, x);
        }
    }

    private <Q extends Query> void invert(int node, int x, IntHandler<Q> handler) {
        odd[node] = !odd[node];
        if (odd[node]) {
            handler.add(node, x);
        } else {
            handler.remove(node, x);
        }
    }

    public <T, Q extends VersionQuery> void handle(T[] data, Modify<T>[] modifies, Q[] queries, Handler<T, Q> handler) {
        handle(data, modifies, queries, handler, (int) Math.ceil(Math.pow(eulerTour.length, 2.0 / 3)));
    }

    public <T, Q extends VersionQuery> void handle(T[] data, Modify<T>[] modifies, Q[] queries, Handler<T, Q> handler,
                                                   int blockSize) {
        if (data.length == 0 || queries.length == 0) {
            return;
        }

        QueryWrapper<Q>[] wrappers = new QueryWrapper[queries.length];
        for (int i = 0; i < queries.length; i++) {
            Q q = queries[i];
            wrappers[i] = new QueryWrapper<Q>();
            wrappers[i].q = q;
            int u = q.getU();
            int v = q.getV();
            int ul = begin[u];
            int ur = end[u];
            int vl = begin[v];
            int vr = end[v];

            if (ur > vr) {
                int tmp = ul;
                ul = vl;
                vl = tmp;

                tmp = ur;
                ur = vr;
                vr = tmp;
            }

            if (ur < vl) {
                wrappers[i].l = ur;
                wrappers[i].r = vl;
                wrappers[i].extra = end[lcaOnTree.lca(u, v)];
            } else {
                wrappers[i].l = ur;
                wrappers[i].r = vr - 1;
                wrappers[i].extra = vr;
            }
        }

        Arrays.fill(odd, false);
        Arrays.sort(wrappers, (a, b) -> {
            int ans = a.l / blockSize - b.l / blockSize;
            if (ans == 0) {
                ans = a.q.getVersion() / blockSize - b.q.getVersion() / blockSize;
            }
            if (ans == 0) {
                ans = a.r - b.r;
            }
            return ans;
        });

        int l = wrappers[0].l;
        int r = l - 1;
        int v = 0;
        for (QueryWrapper<Q> wrapper : wrappers) {
            int ll = wrapper.l;
            int rr = wrapper.r;
            int vv = wrapper.q.getVersion();

            while (v < vv) {
                modifies[v].apply(data, handler, odd);
                v++;
            }
            while (v > vv) {
                v--;
                modifies[v].revoke(data, handler, odd);
            }
            while (l > ll) {
                l--;
                invert(eulerTour[l], data[eulerTour[l]], handler);
            }
            while (r < rr) {
                r++;
                invert(eulerTour[r], data[eulerTour[r]], handler);
            }
            while (l < ll) {
                invert(eulerTour[l], data[eulerTour[l]], handler);
                l++;
            }
            while (r > rr) {
                invert(eulerTour[r], data[eulerTour[r]], handler);
                r--;
            }
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
            handler.answer(wrapper.q);
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
        }
    }

    public <Q extends VersionQuery> void handle(int[] data, IntModify[] modifies, Q[] queries, IntHandler<Q> handler) {
        handle(data, modifies, queries, handler, (int) Math.ceil(Math.pow(eulerTour.length, 2.0 / 3)));
    }

    public <Q extends VersionQuery> void handle(int[] data, IntModify[] modifies, Q[] queries, IntHandler<Q> handler,
                                                int blockSize) {
        if (data.length == 0 || queries.length == 0) {
            return;
        }

        QueryWrapper<Q>[] wrappers = new QueryWrapper[queries.length];
        for (int i = 0; i < queries.length; i++) {
            Q q = queries[i];
            wrappers[i] = new QueryWrapper<Q>();
            wrappers[i].q = q;
            int u = q.getU();
            int v = q.getV();
            int ul = begin[u];
            int ur = end[u];
            int vl = begin[v];
            int vr = end[v];

            if (ur > vr) {
                int tmp = ul;
                ul = vl;
                vl = tmp;

                tmp = ur;
                ur = vr;
                vr = tmp;
            }

            if (ur < vl) {
                wrappers[i].l = ur;
                wrappers[i].r = vl;
                wrappers[i].extra = end[lcaOnTree.lca(u, v)];
            } else {
                wrappers[i].l = ur;
                wrappers[i].r = vr - 1;
                wrappers[i].extra = vr;
            }
        }

        Arrays.fill(odd, false);
        Arrays.sort(wrappers, (a, b) -> {
            int ans = a.l / blockSize - b.l / blockSize;
            if (ans == 0) {
                ans = a.q.getVersion() / blockSize - b.q.getVersion() / blockSize;
            }
            if (ans == 0) {
                ans = a.r - b.r;
            }
            return ans;
        });

        int l = wrappers[0].l;
        int r = l - 1;
        int v = 0;
        for (QueryWrapper<Q> wrapper : wrappers) {
            int ll = wrapper.l;
            int rr = wrapper.r;
            int vv = wrapper.q.getVersion();

            while (v < vv) {
                modifies[v].apply(data, handler, odd);
                v++;
            }
            while (v > vv) {
                v--;
                modifies[v].revoke(data, handler, odd);
            }
            while (l > ll) {
                l--;
                invert(eulerTour[l], data[eulerTour[l]], handler);
            }
            while (r < rr) {
                r++;
                invert(eulerTour[r], data[eulerTour[r]], handler);
            }
            while (l < ll) {
                invert(eulerTour[l], data[eulerTour[l]], handler);
                l++;
            }
            while (r > rr) {
                invert(eulerTour[r], data[eulerTour[r]], handler);
                r--;
            }
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
            handler.answer(wrapper.q);
            invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
        }
    }

    private static class QueryWrapper<Q extends Query> {
        int l;
        int r;
        int extra;
        Q q;
    }

    public interface Query {
        int getU();

        int getV();
    }

    public interface Handler<T, Q extends Query> {
        void add(int node, T x);

        void remove(int node, T x);

        void answer(Q q);
    }

    public interface IntHandler<Q extends Query> {
        void add(int node, int x);

        void remove(int node, int x);

        void answer(Q q);
    }

    public interface VersionQuery extends Query {
        int getVersion();
    }

    public interface Modify<T> {
        <Q extends VersionQuery> void apply(T[] data, Handler<T, Q> handler, boolean[] exists);

        <Q extends VersionQuery> void revoke(T[] data, Handler<T, Q> handler, boolean[] exists);
    }

    public interface IntModify {
        <Q extends VersionQuery> void apply(int[] data, IntHandler<Q> handler, boolean[] exists);

        <Q extends VersionQuery> void revoke(int[] data, IntHandler<Q> handler, boolean[] exists);
    }
}
