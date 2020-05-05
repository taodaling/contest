package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class CPointsLinesAndReadyMadeTitles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
        }

        IntegerHashMap x = new IntegerHashMap(n, false);
        IntegerHashMap y = new IntegerHashMap(n, false);
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            if (x.containKey(pts[i].x)) {
                dsu.merge(x.get(pts[i].x), i);
            }
            if (y.containKey(pts[i].y)) {
                dsu.merge(y.get(pts[i].y), i);
            }
            x.put(pts[i].x, i);
            y.put(pts[i].y, i);
        }

        IntegerMultiWayStack xStack = new IntegerMultiWayStack(n, n);
        IntegerMultiWayStack yStack = new IntegerMultiWayStack(n, n);
        for (int i = 0; i < n; i++) {
            xStack.addLast(dsu.find(i), pts[i].x);
            yStack.addLast(dsu.find(i), pts[i].y);
        }
        IntegerList xList = new IntegerList(n);
        IntegerList yList = new IntegerList(n);

        Modular mod = new Modular(1e9 + 7);
        Combination comb = new Combination(2 * n, mod);
        int ans = 1;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            xList.clear();
            yList.clear();
            xList.addAll(xStack.iterator(i));
            yList.addAll(yStack.iterator(i));
            xList.unique();
            yList.unique();

            int line = xList.size() + yList.size();
            int num = dsu.size[i];
            int local = 0;
            for (int j = 0; j <= num; j++) {
                local = mod.plus(local, comb.combination(line, j));
            }

            ans = mod.mul(ans, local);
        }

        out.println(ans);
    }
}

class Point {
    int x;
    int y;
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
