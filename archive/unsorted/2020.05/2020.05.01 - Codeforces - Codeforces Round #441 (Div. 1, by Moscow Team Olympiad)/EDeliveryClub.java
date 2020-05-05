package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

public class EDeliveryClub {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s1 = in.readInt();
        int s2 = in.readInt();
        int[] x = new int[n + 2];
        x[0] = s1;
        x[1] = s2;
        for (int i = 0; i < n; i++) {
            x[i + 2] = in.readInt();
        }

        DSU dsu = new DSU(x.length, x);
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                dsu.reset();

                for (int i = 1; i < x.length; i++) {
                    while (dsu.lIndex[dsu.find(i)] > 0) {
                        int l = dsu.lIndex[dsu.find(i)] - 1;
                        if (Math.abs(x[l] - dsu.min[dsu.find(i)]) <= mid &&
                                Math.abs(x[l] - dsu.max[dsu.find(i)]) <= mid) {
                            break;
                        }
                        dsu.merge(i, l);
                    }
                }

                return dsu.find(0) != dsu.find(1);
            }
        };

        int ans = ibs.binarySearch(0, (int) 1e9);
        out.println(ans);
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    int[] min;
    int[] max;
    int[] x;
    int[] lIndex;

    public DSU(int n, int[] x) {
        p = new int[n];
        rank = new int[n];
        min = new int[n];
        max = new int[n];
        lIndex = new int[n];
        this.x = x;
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            min[i] = x[i];
            max[i] = x[i];
            lIndex[i] = i;
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

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        p[b] = a;
        min[a] = Math.min(min[a], min[b]);
        max[a] = Math.max(max[a], max[b]);
        lIndex[a] = Math.min(lIndex[a], lIndex[b]);
    }
}
