package on2020_11.on2020_11_12_CSES___CSES_Problem_Set.Intersection_Points;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SegTree;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntersectionPoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<Segment> hs = new ArrayList<>(n);
        List<Segment> vs = new ArrayList<>(n);
        IntegerArrayList xs = new IntegerArrayList(2 * n);
        IntegerArrayList ys = new IntegerArrayList(2 * n);
        for (int i = 0; i < n; i++) {
            int x1 = in.readInt();
            int y1 = in.readInt();
            int x2 = in.readInt();
            int y2 = in.readInt();
            xs.add(x1);
            xs.add(x2);
            ys.add(y1);
            ys.add(y2);
            Segment seg = new Segment();
            if (x1 == x2) {
                vs.add(seg);
                seg.l = y1;
                seg.r = y2;
                seg.h = x1;
            } else {
                hs.add(seg);
                seg.l = x1;
                seg.r = x2;
                seg.h = y1;
            }
        }
        xs.unique();
        ys.unique();
        for (Segment seg : hs) {
            seg.h = ys.binarySearch(seg.h) + 1;
            seg.l = xs.binarySearch(seg.l) + 1;
            seg.r = xs.binarySearch(seg.r) + 1;
        }
        for (Segment seg : vs) {
            seg.h = xs.binarySearch(seg.h) + 1;
            seg.l = ys.binarySearch(seg.l) + 1;
            seg.r = ys.binarySearch(seg.r) + 1;
        }
        //Got all segments
        Segment[] hsOrderByL = hs.toArray(new Segment[0]);
        Segment[] hsOrderByR = hs.toArray(new Segment[0]);
        Arrays.sort(hsOrderByL, (a, b) -> Integer.compare(a.l, b.l));
        Arrays.sort(hsOrderByR, (a, b) -> Integer.compare(a.r, b.r));
        SimplifiedDeque<Segment> ldq = new Range2DequeAdapter<>(i -> hsOrderByL[i], 0, hsOrderByL.length - 1);
        SimplifiedDeque<Segment> rdq = new Range2DequeAdapter<>(i -> hsOrderByR[i], 0, hsOrderByR.length - 1);
        vs.sort((a, b) -> Integer.compare(a.h, b.h));
        long ans = 0;
        int m = ys.size();
        IntegerBIT bit = new IntegerBIT(m);
        for (Segment v : vs) {
            while (!ldq.isEmpty() && ldq.peekFirst().l <= v.h) {
                bit.update(ldq.removeFirst().h, 1);
            }
            while (!rdq.isEmpty() && rdq.peekFirst().r < v.h) {
                bit.update(rdq.removeFirst().h, -1);
            }
            ans += bit.query(v.l, v.r);
        }
        out.println(ans);
    }
}

class Segment {
    int l;
    int r;
    int h;
}