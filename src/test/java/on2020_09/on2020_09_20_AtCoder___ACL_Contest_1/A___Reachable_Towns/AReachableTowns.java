package on2020_09.on2020_09_20_AtCoder___ACL_Contest_1.A___Reachable_Towns;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

public class AReachableTowns {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
            pts[i].id = i;
        }
        Point[] sorted = pts.clone();
        Arrays.sort(sorted, (a, b) -> -Integer.compare(a.x, b.x));
        DSUExt dsu = new DSUExt(n);
        dsu.reset();
        TreeSet<Point> map = new TreeSet<>((a, b) -> Integer.compare(a.y, b.y));
        for (Point pt : sorted) {
            Point largest = null;
            while (true) {
                Point ceil = map.ceiling(pt);
                if (ceil == null) {
                    break;
                }
                largest = ceil;
                dsu.merge(pt.id, ceil.id);
                map.remove(ceil);
            }
            if (largest != null) {
                map.add(largest);
            } else {
                map.add(pt);
            }
        }

        for(Point pt : pts){
            out.println(dsu.size[dsu.find(pt.id)]);
        }
    }
}

class Point {
    int id;
    int x;
    int y;
    int ans;
}

class DSUExt extends DSU {
    int[] size;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    @Override
    public void reset() {
        super.reset();
        Arrays.fill(size, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        size[a] += size[b];
    }
}
