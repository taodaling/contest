package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class OstapAndPartners {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DeltaDSU dsu = new DeltaDSU(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int d = in.readInt();

            if (dsu.find(a) == dsu.find(b)) {
                if (dsu.delta(a, b) != d) {
                    out.printf("Impossible after %d statements", i + 1);
                    return;
                }
                continue;
            }

            dsu.merge(a, b, d);
            if (dsu.delta(dsu.min[dsu.find(0)], 0) < 0) {
                out.printf("Impossible after %d statements", i + 1);
                return;
            }
        }

        out.println("Possible");
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != dsu.find(0)) {
                dsu.merge(dsu.min[dsu.find(i)], 0, 0);
            }
        }

        for (int i = 0; i < n; i++) {
            out.println(dsu.delta(i, 0));
        }
    }
}

class DeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;
    int[] min;


    public DeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
        min = new int[n];
        reset();
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            delta[i] = 0;
            min[i] = i;
        }
    }

    public int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] += delta[p[a]];
        return p[a] = find(p[a]);
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b) {
        if (find(a) != find(b)) {
            throw new IllegalArgumentException();
        }
        return delta[a] - delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d - delta[a] + delta[b];
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
            d = -d;
        }
        p[b] = a;
        delta[b] = -d;
        if (delta(min[a], a) > delta(min[b], a)) {
            min[a] = min[b];
        }
    }
}
