package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.*;

public class DenseGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int x = in.ri();
        int y = in.ri();

        IntegerArrayList list = new IntegerArrayList(m * 4 + 2);
        Interval[] ab = new Interval[m];
        Interval[] cd = new Interval[m];
        for (int i = 0; i < m; i++) {
            ab[i] = new Interval(in.ri(), in.ri());
            list.add(ab[i].l);
            list.add(ab[i].r);
            cd[i] = new Interval(in.ri(), in.ri());
            list.add(cd[i].l);
            list.add(cd[i].r);
        }
        list.add(x);
        list.add(y);
        list.unique();
        int k = list.size();
        x = list.binarySearch(x);
        y = list.binarySearch(y);
        for (Interval p : ab) {
            p.l = list.binarySearch(p.l);
            p.r = list.binarySearch(p.r);
        }
        for (Interval p : cd) {
            p.l = list.binarySearch(p.l);
            p.r = list.binarySearch(p.r);
        }

        Segment st = new Segment(0, k - 1);
        for (int i = 0; i < m; i++) {
            st.add(ab[i].l, ab[i].r, 0, k - 1, cd[i]);
            //st.add(r.b.l, r.b.r, 0, k - 1, r.a);
        }

        TreeSet<Integer> remain = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            remain.add(i);
        }
        int inf = (int) 1e9;
        int[] dist = new int[k];
        Arrays.fill(dist, inf);
        dist[x] = 0;
        remain.remove(x);
        IntegerDeque dq = new IntegerDequeImpl(k);
        dq.addLast(x);
        List<Interval> events = new ArrayList<>(m);
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            events.clear();
            st.query(head, head, 0, k - 1, events);
            for (Interval interval : events) {
                if (interval.visited) {
                    continue;
                }
                interval.visited = true;
                while (true) {
                    Integer next = remain.ceiling(interval.l);
                    if (next == null || next > interval.r) {
                        break;
                    }
//                    if (dist[next] <= dist[head] + 1) {
//                        continue;
//                    }
                    dist[next] = dist[head] + 1;
                    remain.remove(next);
                    dq.addLast(next);
                }
            }
        }

        out.println(dist[y] == inf ? -1 : dist[y]);
    }
}


class Interval {
    int l;
    int r;
    boolean visited;

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "l=" + l +
                ", r=" + r +
                '}';
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    List<Interval> ranges = new ArrayList<>();

    private void modify(Interval r) {
        ranges.add(r);
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void add(int L, int R, int l, int r, Interval range) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(range);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.add(L, R, l, m, range);
        right.add(L, R, m + 1, r, range);
        pushUp();
    }

    public void query(int L, int R, int l, int r, List<Interval> collector) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (!ranges.isEmpty()) {
            collector.addAll(ranges);
            ranges.clear();
        }
        if (enter(L, R, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(L, R, l, m, collector);
        right.query(L, R, m + 1, r, collector);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}

