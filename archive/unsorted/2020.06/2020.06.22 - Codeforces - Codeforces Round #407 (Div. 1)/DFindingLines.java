package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;

import java.util.*;

public class DFindingLines {
    int interval = 19000;

    int inf = (int) 1e8;

    Comparator<Interval> comp = (a, b) -> a.len == b.len ? Integer.compare(a.l, b.l) : -Integer.compare(a.len, b.len);
    PriorityQueue<Interval> ySet = new PriorityQueue<>((int) 1e4, comp);
    List<Integer> yLines = new ArrayList<>();
    TreeSet<Interval> xSet = new TreeSet<>(comp);
    List<Integer> xLines = new ArrayList<>();
    FastInput in;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        clear();
        for (int i = -inf; i <= inf; i += interval) {
            int l = i;
            int r = Math.min(inf, i + interval - 1);
            check(new Interval(l, r), false, 0);
        }
        xLines.sort(Comparator.naturalOrder());
        int[] xArrays = xLines.stream().filter(x -> exist(x, false, 2))
                .mapToInt(Integer::intValue).toArray();

        clear();
        ySet.clear();
        int left = -inf;
        for (int x : xArrays) {
            Interval interval = new Interval(left, x - 1);
            if (!interval.isEmpty()) {
                ySet.add(interval);
            }
            left = x + 1;
        }
        {
            Interval interval = new Interval(left, inf);
            if (!interval.isEmpty()) {
                ySet.add(interval);
            }
        }

        for (int i = -inf; i <= inf; i += interval) {
            int l = i;
            int r = Math.min(inf, i + interval - 1);
            check(new Interval(l, r), true, 0);
        }
        xLines.sort(Comparator.naturalOrder());
        int[] yArrays = xLines.stream().filter(x -> exist(x, true, 2))
                .mapToInt(Integer::intValue).toArray();

        out.printf("1 %d %d", xArrays.length, yArrays.length).println();
        for (int x : xArrays) {
            out.append(x).append(' ');
        }
        out.println();
        for (int y : yArrays) {
            out.append(y).append(' ');
        }
        out.println().flush();
    }

    public void clear() {
        ySet.clear();
        xSet.clear();
        yLines.clear();
        xLines.clear();
        ySet.add(new Interval(-inf, inf));
        xSet.add(new Interval(-inf, inf));
    }

    RandomWrapper rw = new RandomWrapper(new Random());

    public void check(Interval x, boolean inv, int depth) {
        if (x.isEmpty()) {
            return;
        }

        Interval y = ySet.peek();
        int cx = x.middle();
        int cy = y.middle();
        int dist = ask(cx, cy, inv);

        if (!(cx - dist >= x.l || cx + dist <= x.r)) {
            return;
        }

        if (cx - dist >= x.l && exist(cx - dist, inv, 2)) {
            xLines.add(cx - dist);
            for (Interval interval : x.split(cx - dist)) {
                check(interval, inv, depth + 1);
            }
            return;
        }

        if (cx + dist <= x.r && exist(cx + dist, inv, 2)) {
            xLines.add(cx + dist);
            for (Interval interval : x.split(cx + dist)) {
                check(interval, inv, depth + 1);
            }
            return;
        }

        if (cy - dist >= y.l && exist(cy - dist, !inv, 2)) {
            ySet.remove();
            for (Interval interval : y.split(cy - dist)) {
                if (!interval.isEmpty()) {
                    ySet.add(interval);
                }
            }
        }

        if (cy + dist <= y.r && exist(cy + dist, !inv, 2)) {
            ySet.remove();
            for (Interval interval : y.split(cy + dist)) {
                if (!interval.isEmpty()) {
                    ySet.add(interval);
                }
            }
        }

        //check(x, inv, depth + 1);
    }

    public boolean exist(int x, boolean inv, int time) {
        for (int i = 0; i < time; i++) {
            int y1 = rw.nextInt(-inf, inf);
            if (ask(x, y1, inv) != 0) {
                return false;
            }
        }
        return true;
    }

    int limit = (int) 3e5;

    public int ask(int x, int y, boolean inv) {
        if (inv) {
            int tmp = x;
            x = y;
            y = tmp;
        }

        if (limit-- == 0) {
            throw new RuntimeException();
        }

        out.append("0 ").append(x).append(' ').append(y).println().flush();
        return in.readInt();
    }
}

class Interval {
    int l;
    int r;
    int len;

    public int dist(int x) {
        return Math.min(x - l, r - x);
    }

    public int middle() {
        return (r + l) / 2;
    }

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
        this.len = r - l + 1;
    }

    public boolean isEmpty() {
        return r < l;
    }

    public Interval[] split(int x) {
        return new Interval[]{new Interval(l, x - 1), new Interval(x + 1, r)};
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", l, r);
    }
}
