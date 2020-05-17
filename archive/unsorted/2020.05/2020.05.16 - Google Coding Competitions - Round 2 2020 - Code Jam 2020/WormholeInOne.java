package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.Debug;

public class WormholeInOne {
    DSU[] dsus = new DSU[10000];

    {
        for (int i = 0; i < 10000; i++) {
            dsus[i] = new DSU(100);
        }
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
//        debug.debug("", DigitUtils.asLong(Integer.MIN_VALUE, 0));
//        debug.debug("", DigitUtils.asLong(Integer.MIN_VALUE, Integer.MIN_VALUE) == Long.MIN_VALUE);
//        debug.debug("", DigitUtils.asLong(-1, -1));
//        debug.debug("", Long.toBinaryString(DigitUtils.asLong(-1, -1)));
        out.printf("Case #%d: ", testNumber);

        int n = in.readInt();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.readInt();
            }
        }

        LongHashMap map = new LongHashMap(n * n, false);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long key = dir(pts[i], pts[j]);
                int index = (int) map.getOrDefault(key, -1);
                if (index == -1) {
                    index = map.size();
                    dsus[index].reset();
                    map.put(key, index);
                }
                dsus[index].merge(i, j);
            }
        }


//        for (int i = 0; i < n; i++) {
//            debug.debug("i", i);
//            debug.debug("dsu", dsus[0].find(i));
//        }
        int m = map.size();
        int ans = 1;
        for (int i = 0; i < m; i++) {
            int local = 0;
            int oneCnt = 0;
            int odd = 0;
            for (int j = 0; j < n; j++) {
                if (dsus[i].find(j) != j) {
                    continue;
                }
                int size = dsus[i].size[j];
                if (size == 1) {
                    oneCnt++;
                } else {
                    odd += size % 2;
                    local += size;
                }
            }
            local = local + Math.min(oneCnt, 2 - odd % 2);

            ans = Math.max(ans, local);
        }

        out.println(ans);
    }

    public long dir(int[] a, int[] b) {
        int dx = a[0] - b[0];
        int dy = a[1] - b[1];
        if (dy < 0) {
            dy = -dy;
            dx = -dx;
        }
        if (dx < 0 && dy == 0) {
            dx = -dx;
        }
        int g = GCDs.gcd(Math.abs(dx), Math.abs(dy));
        dx /= g;
        dy /= g;
        return DigitUtils.asLong(dx, dy);
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

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        size[a] += size[b];
        p[b] = a;
    }
}
