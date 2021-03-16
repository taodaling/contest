package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.string.KMPAutomaton;
import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.List;
import java.util.TreeSet;

public class SubstringProbability {
    int mod = 998244353;
    char[] p = new char[(int) 1e5];
    char[] q = new char[(int) 1e5];
    Power pow = new Power(mod);

    Pair<int[], int[]> match(int n, int m) {
        KMPAutomaton kmp = new KMPAutomaton(n);
        for (int i = 0; i < n; i++) {
            kmp.build(p[i]);
        }
        kmp.beginMatch();
        int[] fa = new int[n];
        for (int i = 0; i < n; i++) {
            fa[i] = kmp.maxBorder(i) - 1;
        }
        int[] headMatch = new int[m];
        for (int i = 0; i < m; i++) {
            kmp.match(q[i]);
            headMatch[i] = kmp.matchLast - 1;
        }
        return new Pair<>(fa, headMatch);
    }

    List<DirectedEdge>[] buildTree(int[] p) {
        int n = p.length;
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            if (p[i] == -1) {
                continue;
            }
            Graph.addEdge(g, p[i], i);
        }
        return g;
    }

    Debug debug = new Debug(true);
    List<DirectedEdge>[] g;
    int[] dfn;
    int[] invDfn;
    int[] depth;
    int order;
    LcaOnTree lca;
    int[] sets;
    List<Integer>[] events;

    public Subtree merge(Subtree a, Subtree b) {
        if (a.set.size() > b.set.size()) {
            Subtree tmp = a;
            a = b;
            b = tmp;
        }
        for (Integer cand : a.set) {
            add(b, invDfn[cand]);
        }
        return b;
    }

    public void add(Subtree a, int node) {
        Integer floor = a.set.floor(dfn[node]);
        Integer ceil = a.set.ceiling(dfn[node]);
        int l = -1;
        if (floor != null) {
            int cand = invDfn[floor];
            if (sets[cand] == sets[node]) {
                l = lca.lca(cand, node);
            }
        }
        if (ceil != null) {
            int cand = invDfn[ceil];
            if (sets[cand] == sets[node]) {
                int l2 = lca.lca(cand, node);
                if (l == -1 || depth[l] <= depth[l2]) {
                    l = l2;
                }
            }
        }
        int contrib;
        if (l != -1) {
            contrib = depth[node] - depth[l];
        } else {
            contrib = depth[node] + 1;
        }
        if (contrib > 0) {
            a.cover += contrib;
            a.set.add(dfn[node]);
        }
    }

    public void dfsForDepth(int root, int d, int top) {
        depth[root] = d;
        dfn[root] = order++;
        invDfn[dfn[root]] = root;
        sets[root] = top;
        for (DirectedEdge e : g[root]) {
            dfsForDepth(e.to, d + 1, top);
        }
    }

    long total;

    public Subtree dfsForMerge(int root) {
        Subtree tree = new Subtree();
        for (Integer e : events[root]) {
            add(tree, e);
        }
        for (DirectedEdge e : g[root]) {
            tree = merge(tree, dfsForMerge(e.to));
        }
        total += tree.cover;
        return tree;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(p);
        int m = in.rs(q);
        Pair<int[], int[]> head = match(n, m);
        SequenceUtils.reverse(p, 0, n - 1);
        SequenceUtils.reverse(q, 0, m - 1);
        Pair<int[], int[]> tail = match(n, m);
        SequenceUtils.reverse(tail.b);
        List<DirectedEdge>[] headG = buildTree(head.a);
        List<DirectedEdge>[] tailG = buildTree(tail.a);
        dfn = new int[n];
        invDfn = new int[n];
        sets = new int[n];
        depth = new int[n];
        lca = new LcaOnTree(tailG, i -> tail.a[i] == -1);
        g = tailG;
        order = 0;
        for (int i = 0; i < n; i++) {
            if (tail.a[i] == -1) {
                dfsForDepth(i, 0, i);
            }
        }
        g = headG;
        total = 0;
        events = Graph.createGraph(n);
        for (int i = 0; i + 1 < m; i++) {
            if (head.b[i] >= 0 && tail.b[i + 1] >= 0) {
                events[head.b[i]].add(tail.b[i + 1]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (head.a[i] == -1) {
                dfsForMerge(i);
            }
        }
        long all = (long) n * n % mod;
        total %= mod;
        long prob = total * pow.inverse((int) all) % mod;
        out.println(prob);
    }
}

class Subtree {
    TreeSet<Integer> set = new TreeSet<>();
    int cover;
}