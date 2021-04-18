package on2021_03.on2021_03_31_Single_Round_Match_803.MarriageAndGamingChallenge;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.math.DigitUtils;
import template.rand.DenseMultiSetHasher;

import java.util.List;

public class MarriageAndGamingChallenge {
    long state;

    long rnd() {
        state = (state * 1103515245 + 12345) % (1L << 31);
        return state;
    }


    DenseMultiSetHasher hasher;
    NoTagPersistentSegment[] st;
    List<DirectedEdge>[] g;
    int[] weight;
    int N;

    public void dfs(int root, NoTagPersistentSegment p) {
        st[root] = p.clone();
        st[root].update(weight[root], weight[root], 0, N, hasher.hash(weight[root]));
        for (DirectedEdge e : g[root]) {
            dfs(e.to, st[root]);
        }
    }


    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }


    public int firstOdd(NoTagPersistentSegment a, NoTagPersistentSegment b, int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r) || (a.hash ^ b.hash) == 0) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int ans = firstOdd(a.right, b.right, ll, rr, m + 1, r);
        if (ans == -1) {
            ans = firstOdd(a.left, b.left, ll, rr, l, m);
        }
        return ans;
    }

//    Debug debug = new Debug(true);
    public long solve(int N, int D, int state, int B, int P, int Q) {
        this.N = N;
        this.state = state;
        hasher = new DenseMultiSetHasher(0, N);
        int[] parent = new int[N];
        st = new NoTagPersistentSegment[N];
        for (int i = 1; i <= N - 1; i++) {
            parent[i] = (int) Math.max(0, i - 1 - (rnd() % D));
        }
        int[] bank = new int[B];
        for (int i = 0; i <= B - 1; i++) {
            bank[i] = (int) (rnd() % N);
        }
        weight = new int[N];
        for (int i = 1; i <= N - 1; i++) {
            if (rnd() % 100 < P) {
                weight[i] = (int) (rnd() % N);
            } else {
                weight[i] = bank[(int) (rnd() % B)];
            }
        }
        g = Graph.createGraph(N);
        for (int i = 1; i < N; i++) {
            Graph.addEdge(g, parent[i], i);
        }
        dfs(0, NoTagPersistentSegment.NIL);

        long ans = 0;
        for (int q = 0; q <= Q - 1; q++) {
            int U = (int) (rnd() % N);
            int V = (int) (rnd() % N);
            int hi = (int) (rnd() % N);
            int lo = firstOdd(st[U], st[V], 0, hi, 0, N);
            if (lo <= hi) {
                ans += hi - lo;
            }
//            debug.debug("U", U);
//            debug.debug("V", V);
//            debug.debug("l", l);
//            debug.debug("hi", hi);
//            debug.debug("lo", lo);
        }
        return ans;
    }
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    public NoTagPersistentSegment left;
    public NoTagPersistentSegment right;
    public long hash;

    public void pushUp() {
        hash = left.hash ^ right.hash;
    }

    public NoTagPersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long h) {
        if (covered(ll, rr, l, r)) {
            hash ^= h;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, h);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, h);
        }
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    @Override
    public NoTagPersistentSegment clone() {
        try {
            return (NoTagPersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
