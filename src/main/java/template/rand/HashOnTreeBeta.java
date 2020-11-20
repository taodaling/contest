package template.rand;

import template.graph.Centroid;
import template.graph.DirectedEdge;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.util.Arrays;
import java.util.List;

public class HashOnTreeBeta {
    static int mod = (int) 1e9 + 7;

    Centroid centroid;
    List<? extends DirectedEdge>[] g;
    int seed1;
    int seed2;
    long[] buf;
    int bufLen;
    long[] hash;
    int[] size;
    int[] p;

    private long bufHash() {
        Randomized.shuffle(buf, 0, bufLen);
        Arrays.sort(buf, 0, bufLen);
        long sum1 = 1;
        long sum2 = 1;
        for (int i = 0; i < bufLen; i++) {
            sum1 = (sum1 * seed1 + buf[i]) % mod;
            sum2 = (sum2 * seed2 + buf[i]) % mod;
        }
        return DigitUtils.asLong((int) sum1, (int) sum2);
    }

    public HashOnTreeBeta(int seed1, int seed2, int n) {
        this.seed1 = seed1;
        this.seed2 = seed2;
        buf = new long[n];
        hash = new long[n];
        size = new int[n];
        p = new int[n];
        centroid = new Centroid(n);
    }

    public void init(List<? extends DirectedEdge>[] g) {
        this.g = g;
        for (int i = 0; i < g.length; i++) {
            p[i] = -2;
        }
    }

    public long hashOnRoot(int root) {
        return hashOnRoot(root, -1);
    }

    public long hashOnRoot(int root, int p) {
        if (this.p[root] == p) {
            return hash[root];
        }
        this.p[root] = p;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            hashOnRoot(e.to, root);
        }
        bufLen = 0;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            buf[bufLen++] = hash[e.to];
        }
        hash[root] = bufHash();
        return hash[root];
    }

    IntegerArrayList cand = new IntegerArrayList(2);
    LongArrayList hashVal = new LongArrayList(2);

    public long hashTree() {
        hashVal.clear();
        cand.clear();
        centroid.findCentroid(g, root -> {
            cand.add(root);
        });
        for (int root : cand.toArray()) {
            hashVal.add(hashOnRoot(root, -1));
        }
        bufLen = hashVal.size();
        for (int i = 0; i < hashVal.size(); i++) {
            buf[i] = hashVal.get(i);
        }
        return bufHash();
    }
}
