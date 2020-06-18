package contest;

import template.datastructure.BitSet;
import template.geometry.geo2.IntegerPoint2;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MilitaryStory {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.readInt(), in.readInt());
        }

        BitSet used = new BitSet(n);
        BitSet removed = new BitSet(n);
        int fence = 0;
        List<IntegerPoint2> list = new ArrayList<>(n);
        while (true) {
            //all point on same line
            boolean onSameLine = true;
            list.clear();

            used.fill(false);

            int lb = -1;
            for (int i = 0; i < n; i++) {
                if (removed.get(i)) {
                    continue;
                }
                list.add(pts[i]);
                if (lb == -1 || IntegerPoint2.SORT_BY_XY.compare(pts[lb], pts[i]) < 0) {
                    lb = i;
                }
            }

            for (int i = 2; i < list.size() && onSameLine; i++) {
                IntegerPoint2 pt = list.get(i);
                if (IntegerPoint2.orient(list.get(0), list.get(1), pt) != 0) {
                    onSameLine = false;
                }
            }

            if (onSameLine) {
                break;
            }

            fence++;
            used.set(lb);

            IntegerPoint2 last = pts[lb];
            while (true) {
                int pick = -1;
                for (int i = 0; i < n; i++) {
                    if (removed.get(i)) {
                        continue;
                    }
                    if (last == pts[i]) {
                        continue;
                    }
                    if (pick == -1 || IntegerPoint2.orient(last, pts[i], pts[pick]) > 0 ||
                            IntegerPoint2.orient(last, pts[i], pts[pick]) == 0 &&
                                    IntegerPoint2.dist2(last, pts[i]) < IntegerPoint2.dist2(last, pts[pick])) {
                        pick = i;
                    }
                }
                if (used.get(pick)) {
                    break;
                }
                used.set(pick);
                last = pts[pick];
            }

            removed.or(used);
        }

        out.println(fence);
    }
}
