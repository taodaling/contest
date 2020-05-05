package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BSortingTheCoins {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }
        DSU dsu = new DSU(n);
        out.append(1).append(' ');
        boolean[] covered = new boolean[n];
        for (int i = 0; i < n; i++) {
            covered[p[i]] = true;
            if (p[i] - 1 >= 0 && covered[p[i] - 1]) {
                dsu.merge(p[i] - 1, p[i]);
            }
            if (p[i] + 1 < n && covered[p[i] + 1]) {
                dsu.merge(p[i] + 1, p[i]);
            }
            int oneCnt = i + 1;
            if (covered[n - 1]) {
                oneCnt -= dsu.size[dsu.find(n - 1)];
            }
            out.append(oneCnt + 1).append(' ');
        }
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    int[] size;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        size = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }


    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] > rank[b]) {
            p[b] = a;
            size[a] += size[b];
        } else {
            p[a] = b;
            size[b] += size[a];
        }
    }
}
