package template.problem;

import template.datastructure.ActiveSegment;
import template.datastructure.Range2DequeAdapter;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.CloneSupportObject;

import java.util.Arrays;
import java.util.Comparator;

public class RectUnionArea {
    /**
     * <pre>
     * 计算矩阵交的面积
     * 时间复杂度：O(n\log_2n)
     * 空间复杂度：O(n)
     * </pre>
     */
    public static long unionArea(Rect[] rects) {
        LongArrayList list = new LongArrayList(rects.length * 2);
        for (Rect r : rects) {
            list.add(r.l);
            list.add(r.r);
        }
        list.unique();
        int m = list.size();
        if (m <= 1) {
            return 0;
        }
        for (Rect rect : rects) {
            rect.l = list.binarySearch(rect.l);
            rect.r = list.binarySearch(rect.r);
        }
        Rect[] sortByB = rects.clone();
        Rect[] sortByT = rects.clone();
        Arrays.sort(sortByB, Comparator.comparingLong(x -> x.b));
        Arrays.sort(sortByT, Comparator.comparingLong(x -> x.t));
        Range2DequeAdapter<Rect> dqByB = new Range2DequeAdapter<>(i -> sortByB[i], 0, sortByB.length - 1);
        Range2DequeAdapter<Rect> dqByT = new Range2DequeAdapter<>(i -> sortByT[i], 0, sortByT.length - 1);
        ActiveSegment as = new ActiveSegment(0, m - 2);
        as.init(0, m - 2, i -> list.get(i + 1) - list.get(i));
        long totalWeight = as.queryNonActive();
        long last = 0;
        long ans = 0;
        while (!dqByT.isEmpty()) {
            long now = dqByT.peekFirst().t;
            if (!dqByB.isEmpty()) {
                now = Math.min(now, dqByB.peekFirst().b);
            }
            ans += as.queryActive(totalWeight) * (now - last);
            while (!dqByB.isEmpty() && dqByB.peekFirst().b == now) {
                Rect head = dqByB.removeFirst();
                as.update((int) head.l, (int) head.r - 1, 0, m - 2, 1);
            }
            while (!dqByT.isEmpty() && dqByT.peekFirst().t == now) {
                Rect head = dqByT.removeFirst();
                as.update((int) head.l, (int) head.r - 1, 0, m - 2, -1);
            }
            last = now;
        }
        for (Rect rect : rects) {
            rect.l = list.get((int) rect.l);
            rect.r = list.get((int) rect.r);
        }
        return ans;
    }

    public static class Rect extends CloneSupportObject<Rect> {
        public long l;
        public long r;
        public long b;
        public long t;

        public Rect() {
        }

        public Rect(long l, long r, long b, long t) {
            this.l = l;
            this.r = r;
            this.b = b;
            this.t = t;
        }

        public static Rect newInstanceByTwoPoints(long x1, long y1, long x2, long y2) {
            if (x1 > x2) {
                long tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y1 > y2) {
                long tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            return new Rect(x1, x2, y1, y2);
        }

        public static Rect intersect(Rect a, Rect b) {
            Rect ans = new Rect(Math.max(a.l, b.l), Math.min(a.r, b.r), Math.max(a.b, b.b), Math.min(a.t, b.t));
            if (ans.empty()) {
                return null;
            }
            return ans;
        }

        public long area() {
            return empty() ? 0 : (r - l) * (t - b);
        }

        public boolean empty() {
            return r < l || t < b;
        }
    }
}

