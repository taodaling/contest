package contest;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.PriorityQueue;

import template.FastInput;

public class TaskG {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int c = in.readInt();
        int c0 = in.readInt();

        Event[] events = new Event[n + 1];
        events[0] = new Event(c0, 0, 0);

        for (int i = 1; i <= n; i++) {
            int t = in.readInt();
            int a = in.readInt();
            int b = in.readInt();
            events[i] = new Event(a, b, t);
        }

        Arrays.sort(events, (a, b) -> a.time - b.time);
        PriorityQueue<Event> queue = new PriorityQueue<>(n + 1, (a, b) -> a.unitCost - b.unitCost);
        Segment segment = new Segment(0, n);

        for (int i = 0; i < n; i++) {
            events[i].carry = events[i + 1].time - events[i].time;
        }
        events[n].carry = m - events[n].time;

        for (int i = 0; i <= n; i++) {
            events[i].index = i;
        }

        long cost = 0;
        for (Event e : events) {
            queue.add(e);
            while (e.carry > 0 && !queue.isEmpty()) {
                Event top = queue.remove();
                int canUse = c - segment.queryMax(top.index, e.index, 0, n);
                canUse = Math.min(canUse, top.in);
                canUse = Math.min(canUse, e.carry);
                if (canUse == 0) {
                    continue;
                }
                cost += (long) canUse * top.unitCost;
                top.in -= canUse;
                e.carry -= canUse;
                segment.update(top.index, e.index, 0, n, canUse);
                if (top.in > 0) {
                    queue.add(top);
                }
            }
            if (e.carry > 0) {
                out.println(-1);
                return;
            }
        }
        out.println(cost);
    }
}


class Event {
    int in;
    int unitCost;
    int time;
    int carry;
    int index;


    public Event(int in, int unitCost, int time) {
        this.in = in;
        this.unitCost = unitCost;
        this.time = time;
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int val;
    private int plus;

    public void setPlus(int p) {
        this.plus += p;
        this.val += p;
    }

    public void pushUp() {
        val = Math.max(left.val, right.val);
    }

    public void pushDown() {
        if (plus != 0) {
            left.setPlus(plus);
            right.setPlus(plus);
            plus = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int p) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setPlus(p);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, p);
        right.update(ll, rr, m + 1, r, p);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.queryMax(ll, rr, l, m), right.queryMax(ll, rr, m + 1, r));
    }
}
