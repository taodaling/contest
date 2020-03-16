package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.primitve.generated.IntegerHashMap;
import template.primitve.generated.IntegerObjectHashMap;

public class DDeductionQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        int last = 0;

        DeltaDSU dsu = new DeltaDSU(2 * q);
        IntegerHashMap map = new IntegerHashMap(2 * q, false);
        for (int i = 0; i < q; i++) {
            if (in.readInt() == 1) {
                int l = (in.readInt() ^ last);
                int r = (in.readInt() ^ last);

                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                l--;

                int x = in.readInt() ^ last;

                int a = getItems(map, l);
                int b = getItems(map, r);

                if (dsu.find(a) == dsu.find(b)) {
                    continue;
                }

                dsu.merge(a, b, x);
            } else {
                int l = (in.readInt() ^ last);
                int r = (in.readInt() ^ last);

                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                l--;

                int a = getItems(map, l);
                int b = getItems(map, r);

                if (dsu.find(a) != dsu.find(b)) {
                    out.println(-1);
                    last = 1;
                    continue;
                }

                int ans = dsu.delta(a, b);

                out.println(ans);
                last = ans;
            }
        }
    }

    int alloc = 0;

    public int getItems(IntegerHashMap map, int key) {
        int item = map.getOrDefault(key, Integer.MIN_VALUE);
        if (item == Integer.MIN_VALUE) {
            map.put(key, item = alloc++);
        }
        return item;
    }
}


class DeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;

    public DeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
        reset();
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            delta[i] = 0;
        }
    }

    public int find(int a) {
        if(p[a] == p[p[a]]){
            return p[a];
        }
        find(p[a]);
        delta[a] ^= delta[p[a]];
        return p[a] = find(p[a]);
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b){
        find(a);
        find(b);
        return delta[a] ^ delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d ^ delta[a] ^ delta[b];
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
            delta[b] = d;
        } else {
            p[a] = b;
            delta[a] = d;
        }
    }
}
