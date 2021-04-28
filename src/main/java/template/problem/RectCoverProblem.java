package template.problem;

import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class RectCoverProblem {
    private static void update(LongBIT bit, int l, int r, long x) {
        bit.update(l, x);
        if (bit.size() >= r + 1) {
            bit.update(r + 1, -x);
        }
    }

    private static long query(LongBIT bit, int x) {
        return bit.query(x);
    }

    public static long[] solve(Rect[] rects, Point[] pts) {
        if (rects.length == 0 || pts.length == 0) {
            return new long[pts.length];
        }

        int[] indices = IntStream.range(0, pts.length).toArray();
        SortUtils.quickSort(indices, (i, j) -> Long.compare(pts[i].x, pts[j].x), 0, pts.length);
        Rect[] sortByL = rects.clone();
        Rect[] sortByR = rects.clone();
        Arrays.sort(sortByL, Comparator.comparingLong(x -> x.l));
        Arrays.sort(sortByR, Comparator.comparingLong(x -> x.r));
        LongArrayList list = new LongArrayList(2 * rects.length + pts.length);
        for (Rect rect : rects) {
            list.add(rect.b);
            list.add(rect.t);
        }
        for (Point pt : pts) {
            list.add(pt.y);
        }
        list.unique();
        int sortByLHead = 0;
        int sortByRHead = 0;
        int indicesHead = 0;

        long[] ans = new long[pts.length];
        LongBIT bit = new LongBIT(list.size());
        for (int i = 0; i < list.size(); i++) {
            long v = list.get(i);
            while (sortByLHead < sortByL.length && sortByL[sortByLHead].l <= v) {
                Rect head = sortByL[sortByLHead++];
                update(bit, list.binarySearch(head.b) + 1, list.binarySearch(head.t) + 1, head.val);
            }
            while (sortByRHead < sortByR.length && sortByR[sortByRHead].r < v) {
                Rect head = sortByR[sortByRHead++];
                update(bit, list.binarySearch(head.b)  + 1, list.binarySearch(head.t) + 1, -head.val);
            }
            while (indicesHead < indices.length && pts[indices[indicesHead]].x <= v) {
                Point pt = pts[indices[indicesHead]];
                ans[indices[indicesHead]] = query(bit, list.binarySearch(pt.y) + 1);
                indicesHead++;
            }
        }

        return ans;
    }

    public static class Rect {
        public long l;
        public long r;
        public long b;
        public long t;
        public long val;

        public Rect(long l, long r, long b, long t, long val) {
            this.l = l;
            this.r = r;
            this.b = b;
            this.t = t;
            this.val = val;
        }
    }

    public static class Point {
        public long x;
        public long y;
    }
}
