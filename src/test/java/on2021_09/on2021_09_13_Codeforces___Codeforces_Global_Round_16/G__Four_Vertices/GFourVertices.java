package on2021_09.on2021_09_13_Codeforces___Codeforces_Global_Round_16.G__Four_Vertices;



import template.datastructure.MergeAbleCountSegment;
import template.datastructure.TreapT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.Debug;

import java.util.*;

public class GFourVertices {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        cntE.update(R, L, R, 100);
        cntV = new MergeAbleCountSegment[n];
        ts = new TreapT[n];
        Arrays.fill(ts, TreapT.NIL);
        int[][] edges = new int[m][3];
        LongArrayList all = new LongArrayList((int) 2e5);
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.ri() - 1;
            edges[i][1] = in.ri() - 1;
            edges[i][2] = in.ri();
            all.add(edges[i][2]);
        }
        int q = in.ri();
        int[][] qs = new int[q][];
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            qs[i] = new int[3 + t];
            qs[i][0] = t;
            qs[i][1] = in.ri() - 1;
            qs[i][2] = in.ri() - 1;
            if (t == 1) {
                qs[i][3] = in.ri();
                all.add(qs[i][3]);
            }
        }
        all.add((long) 2e10);
        all.unique();
        unique = all.toArray();
        L = 0;
        R = unique.length - 1;

        cntE.update(R, R, R, 100);
        for (int i = 0; i < n; i++) {
            cntV[i] = new MergeAbleCountSegment();
            cntV[i].update(R, L, R, 2);
        }
        for (int[] e : edges) {
            add(e[0], e[1], e[2]);
        }
        out.println(query());
        for (int[] query : qs) {
            int t = query[0];
            int u = query[1];
            int v = query[2];
            if (t == 0) {
                del(u, v);
            } else {
                add(u, v, query[3]);
            }
            out.println(query());
        }
        debug.summary();
    }


    public int kth(MergeAbleCountSegment add, MergeAbleCountSegment sub0,
                   MergeAbleCountSegment sub1, long k, int l, int r) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int num = add.left.cnt - sub0.left.cnt - sub1.left.cnt;
        if (num >= k) {
            return kth(add.left, sub0.left, sub1.left, k, l, m);
        }
        return kth(add.right, sub0.right, sub1.right, k - num, m + 1, r);
    }

    List<Edge> l1 = new ArrayList<>(3);
    List<Edge> l2 = new ArrayList<>(3);

    public long query() {
        long best = (long) 2e10;

        Edge minE = TreapT.getKeyByRank(pq, 1);
        //t1
        {
            cntV[minE.a].update(minE.w, L, R, -1);
            int another = kth(cntE, cntV[minE.a], cntV[minE.b],
                    1, L, R);
            best = Math.min(best, unique[minE.w] + unique[another]);
            cntV[minE.a].update(minE.w, L, R, 1);
        }

        //t2
        {
            TreapT<Edge>[] split1 = TreapT.splitByRank(ts[minE.a], 3);
            TreapT<Edge>[] split2 = TreapT.splitByRank(ts[minE.b], 3);

            l1.clear();
            l2.clear();

            TreapT.collect(split1[0], x -> l1.add(x.key));
            TreapT.collect(split2[0], x -> l2.add(x.key));

            l1.remove(0);
            l2.remove(0);
            for (Edge e1 : l1) {
                for (Edge e2 : l2) {
                    if (e1.other(minE.a) == e2.other(minE.b)) {
                        continue;
                    }
                    long cand = unique[e1.w] + unique[e2.w];
                    best = Math.min(best, cand);
                }
            }
            if (l1.size() == 2) {
                Edge e1 = l1.get(0);
                Edge e2 = l1.get(1);
                long cand = unique[e1.w] + unique[e2.w] + unique[minE.w];
                best = Math.min(best, cand);
            }
            if (l2.size() == 2) {
                Edge e1 = l2.get(0);
                Edge e2 = l2.get(1);
                long cand = unique[e1.w] + unique[e2.w] + unique[minE.w];
                best = Math.min(best, cand);
            }

            ts[minE.a] = TreapT.merge(split1[0], split1[1]);
            ts[minE.b] = TreapT.merge(split2[0], split2[1]);
        }

        return best;
    }

    public TreapT<Edge> add(TreapT<Edge> root, Edge e) {
        TreapT<Edge> node = new TreapT<>(e);
        TreapT<Edge>[] split = TreapT.splitByKey(root, e, comp, true);
        split[0] = TreapT.merge(split[0], node);
        return TreapT.merge(split[0], split[1]);
    }

    public TreapT<Edge> remove(TreapT<Edge> root, Edge e) {
        TreapT<Edge>[] split = TreapT.splitByKey(root, e, comp, true);
        split[0] = TreapT.splitByRank(split[0], split[0].size - 1)[0];
        return TreapT.merge(split[0], split[1]);
    }

    public void add(int a, int b, int x) {
        x = Arrays.binarySearch(unique, x);
        Edge e = new Edge();
        e.v = id(a, b);
        e.a = a;
        e.b = b;
        e.w = x;

        cntE.update(x, L, R, 1);
        cntV[a].update(x, L, R, 1);
        cntV[b].update(x, L, R, 1);

        pq = add(pq, e);
        map.put(id(a, b), e);
        ts[a] = add(ts[a], e);
        ts[b] = add(ts[b], e);
    }

    public void del(int a, int b) {
        Edge e = map.remove(id(a, b));
        pq = remove(pq, e);
        int x = e.w;
        cntE.update(x, L, R, -1);
        cntV[a].update(x, L, R, -1);
        cntV[b].update(x, L, R, -1);
        ts[a] = remove(ts[a], e);
        ts[b] = remove(ts[b], e);
    }

    public long id(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    long[] unique;
    int L = 1;
    int R = (int) 2e9;
    MergeAbleCountSegment cntE = new MergeAbleCountSegment();
    MergeAbleCountSegment[] cntV;
    Comparator<Edge> comp = Comparator.<Edge>comparingInt(x -> x.w)
            .thenComparingLong(x -> x.v);
    TreapT<Edge> pq = TreapT.NIL;
    Map<Long, Edge> map = new HashMap<>((int) 2e5);
    TreapT<Edge>[] ts;
    Debug debug = new Debug(true);
}

class Edge {
    int a;
    int b;
    int w;
    long v;

    public int other(int x) {
        return a ^ b ^ x;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "a=" + a +
                ", b=" + b +
                ", w=" + w +
                '}';
    }
}
