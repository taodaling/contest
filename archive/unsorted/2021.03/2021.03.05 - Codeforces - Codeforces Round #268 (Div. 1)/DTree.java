package contest;

import template.datastructure.SegTree;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class DTree {
    List<IntegerWeightUndirectedEdge>[] g;
    long[] depth;
    LcaOnTree lca;

    void dfsForDepth(int root, int p, long d) {
        depth[root] = d;
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDepth(e.to, root, d + e.weight);
        }
    }

    int[] size;

    void dfsForSize(int root, int p) {
        size[root] = 1;
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSize(e.to, root);
            size[root] += size[e.to];
        }
    }

    int n;

    int dfsForCentroid(int root, int p) {
        int maxSize = n - size[root];
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            int ans = dfsForCentroid(e.to, root);
            if (ans != -1) {
                return ans;
            }
            maxSize = Math.max(maxSize, size[e.to]);
        }
        return maxSize * 2 <= n ? root : -1;
    }

    int[] belong;
    IntegerArrayList collector;

    void collect(int root, int p, int setId) {
        collector.add(root);
        belong[root] = setId;
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            collect(e.to, root, setId);
        }
    }

    public long dist(int a, int b){
        int c = lca.lca(a, b);
        return depth[a] + depth[b] - 2 * depth[c];
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        g = Graph.createGraph(n);
        depth = new long[n];
        size = new int[n];
        belong = new int[n];
        for (int i = 0; i < n - 1; i++) {
            IntegerWeightGraph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1, in.ri());
        }
        lca = new LcaOnTree(n);
        lca.init(g, 0);
        dfsForDepth(0, -1, 0);
        dfsForSize(0, -1);
        int root = dfsForCentroid(0, -1);
        debug.debug("root", root);
        int m = g[root].size() + 1;
        Heap[] heaps = new Heap[m];
        heaps[0] = new Heap(new IntegerArrayList(new int[]{root}), 0);
        belong[root] = 0;
        int wpos = 1;
        collector = new IntegerArrayList(n);
        for (IntegerWeightUndirectedEdge e : g[root]) {
            collector.clear();
            collect(e.to, root, wpos);
            IntegerArrayList data = collector.clone();
            data.sort(IntegerComparator.REVERSE_ORDER);
            heaps[wpos] = new Heap(data, wpos);
            wpos++;
        }
        TreeSet<Heap> pq = new TreeSet<>(Comparator.<Heap>comparingInt(x -> x.back).thenComparingInt(x -> x.id));
        pq.addAll(Arrays.asList(heaps));

        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, m - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.minIndex = i;
                    ans.min = n - heaps[i].size() * 2;
                    if (i == 0) {
                        ans.min = n - 1;
                    }
                    return ans;
                });

        int[] perm = new int[n];
        SumImpl sumBuf = new SumImpl();
        UpdateImpl updateBuf = new UpdateImpl();
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("st", st);
            updateBuf.mod = 1;
            st.update(belong[i], belong[i], 0, m - 1, updateBuf);
            sumBuf.clear();
            st.query(0, m - 1, 0, m - 1, sumBuf);
            Heap match = null;
            if (sumBuf.min == 0) {
                match = heaps[sumBuf.minIndex];
                pq.remove(match);
            } else {
                boolean removed = false;
                if (i != root) {
                    removed = pq.remove(heaps[belong[i]]);
                }
                match = pq.pollFirst();
                if (removed) {
                    pq.add(heaps[belong[i]]);
                }
            }
            perm[i] = match.pop();
            if (match.hasMore()) {
                pq.add(match);
            }
            updateBuf.mod = -1;
            st.update(0, match.id - 1, 0, m - 1, updateBuf);
            st.update(match.id + 1, m - 1, 0, m - 1, updateBuf);
        }

        debug.debug("perm", perm);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += dist(i, perm[i]);
        }
        out.println(sum);
        for(int x : perm){
            out.append(x + 1).append(' ');
        }
    }
}

class Heap {
    IntegerArrayList list;
    int id;
    int back;

    int size() {
        int ans = list.size() + (hasMore() ? 1 : 0);
        return ans;
    }

    public Heap(IntegerArrayList list, int id) {
        this.list = list;
        this.id = id;
        back = list.pop();
    }

    public int pop() {
        int ans = back;
        if (!list.isEmpty()) {
            back = list.pop();
        } else {
            back = -1;
        }
        return ans;
    }

    public boolean hasMore() {
        return back != -1;
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int mod;

    @Override
    public void update(UpdateImpl update) {
        mod += update.mod;
    }

    @Override
    public void clear() {
        mod = 0;
    }

    @Override
    public boolean ofBoolean() {
        return mod != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int min;
    int minIndex;

    void clear() {
        min = Integer.MAX_VALUE;
        minIndex = -1;
    }

    @Override
    public void add(SumImpl sum) {
        if (min > sum.min) {
            min = sum.min;
            minIndex = sum.minIndex;
        }
    }

    @Override
    public void update(UpdateImpl update) {
        min += update.mod;
    }

    @Override
    public void copy(SumImpl sum) {
        this.min = sum.min;
        this.minIndex = sum.minIndex;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + min;
    }
}