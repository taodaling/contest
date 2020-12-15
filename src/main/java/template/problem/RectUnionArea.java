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
            list.add(r.r + 1);
        }
        list.unique();
        int m = list.size();
        Rect[] sortByB = rects.clone();
        Rect[] sortByT = rects.clone();
        Arrays.sort(sortByB, Comparator.comparingLong(x -> x.b));
        Arrays.sort(sortByT, Comparator.comparingLong(x -> x.t));
        Range2DequeAdapter<Rect> dqByB = new Range2DequeAdapter<>(i -> sortByB[i], 0, sortByB.length - 1);
        Range2DequeAdapter<Rect> dqByT = new Range2DequeAdapter<>(i -> sortByT[i], 0, sortByT.length - 1);
        ActiveSegment as = new ActiveSegment(0, m - 2);
        as.init(0, m - 2, i -> list.get(i + 1) - list.get(i));
        long sum = as.queryNonActive();
        long last = 0;
        long ans = 0;
        while (!dqByT.isEmpty()) {
            long now = dqByT.peekFirst().t + 1;
            if (!dqByB.isEmpty()) {
                now = Math.min(now, dqByB.peekFirst().b);
            }
            ans += (sum - as.queryNonActive()) * (now - last);
            while (!dqByB.isEmpty() && dqByB.peekFirst().b == now) {
                Rect head = dqByB.removeFirst();
                as.update(list.binarySearch(head.l), list.binarySearch(head.r + 1) - 1, 0, m - 2, 1);
            }
            while (!dqByT.isEmpty() && dqByT.peekFirst().t + 1 == now) {
                Rect head = dqByT.removeFirst();
                as.update(list.binarySearch(head.l), list.binarySearch(head.r + 1) - 1, 0, m - 2, -1);
            }
            last = now;
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
    }
}

