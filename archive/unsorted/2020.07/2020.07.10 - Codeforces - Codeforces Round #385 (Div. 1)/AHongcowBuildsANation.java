package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AHongcowBuildsANation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        DSU dsu = new DSU(n);
        for (int i = 0; i < k; i++) {
            dsu.special[in.readInt() - 1] = true;
        }

        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(a, b);
        }

        long ans = -m;
        int cnt = 0;
        int max = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            int size = dsu.size[dsu.find(i)];
            ans += choose(size);
            if (dsu.special[dsu.find(i)]) {
                max = Math.max(max, size);
            } else {
                ans += cnt * size;
                cnt += size;
            }
        }

        ans += (long)cnt * max;
        out.println(ans);
    }

    public long choose(long n) {
        return n * (n - 1) / 2;
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    protected boolean[] special;
    int[] size;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        size = new int[n];
        special = new boolean[n];
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

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        special[a] = special[a] || special[b];
        size[a] += size[b];
        p[b] = a;
    }
}

