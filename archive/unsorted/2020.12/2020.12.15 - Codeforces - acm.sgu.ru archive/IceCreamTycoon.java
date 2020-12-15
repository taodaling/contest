package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class IceCreamTycoon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] cmd = new char[100];
        List<Req> reqs = new ArrayList<>((int) 1e5);
        IntegerArrayList list = new IntegerArrayList((int) 1e5);
        while (in.hasMore()) {
            in.rs(cmd, 0);
            if (cmd[0] == 'A') {
                int n = in.ri();
                int c = in.ri();
                reqs.add(new Req(0, n, c));
                list.add(c);
            } else {
                int n = in.ri();
                long t = in.rl();
                reqs.add(new Req(1, n, t));
            }
        }
        if (list.isEmpty()) {
            list.add(0);
        }
        list.unique();
        int m = list.size();
        Segment seg = new Segment(0, m - 1, i -> list.get(i));
        long sum = 0;
        int kth = 0;
        for (Req req : reqs) {
            kth++;
            if (req.type == 0) {
                int n = req.n;
                int c = (int) req.cost;
                sum += n;
                c = list.binarySearch(c);
                seg.update(c, c, 0, m - 1, n);
            } else {
                int n = req.n;
                long t = req.cost;
                if (sum < n) {
                    out.println("UNHAPPY");
                    continue;
                }
                long cost = seg.query(0, m - 1, 0, m - 1, n);
                if (cost <= t) {
                    sum -= n;
                    seg.consume(0, m - 1, 0, m - 1, n);
                    out.println("HAPPY");
                } else {
                    out.println("UNHAPPY");
                }
            }
        }
    }
}

class Req {
    int type;
    int n;
    long cost;

    public Req(int type, int n, long cost) {
        this.type = type;
        this.n = n;
        this.cost = cost;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long cnt;
    long cost;
    boolean clear;
    int weight;

    private void modify(long cnt) {
        this.cnt += cnt;
        this.cost = this.cnt * weight;
    }

    private void clear() {
        clear = true;
        cnt = 0;
        cost = 0;
    }

    public void pushUp() {
        cnt = left.cnt + right.cnt;
        cost = left.cost + right.cost;
    }

    public void pushDown() {
        if (clear) {
            left.clear();
            right.clear();
            clear = false;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            weight = func.apply(l);
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long cnt) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(cnt);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, cnt);
        right.update(ll, rr, m + 1, r, cnt);
        pushUp();
    }

    public void consume(int ll, int rr, int l, int r, long k) {
        if (leave(ll, rr, l, r) || k <= 0) {
            return;
        }
        if (enter(ll, rr, l, r) && cnt <= k) {
            clear();
            return;
        }
        if (l == r) {
            modify(-k);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        long forRight = k - left.cnt;
        left.consume(ll, rr, l, m, k);
        right.consume(ll, rr, m + 1, r, forRight);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r, long k) {
        if (leave(ll, rr, l, r) || k <= 0) {
            return 0;
        }
        if (enter(ll, rr, l, r) && cnt <= k) {
            return cost;
        }
        if (l == r) {
            return weight * k;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        long cost = left.query(ll, rr, l, m, k);
        cost += right.query(ll, rr, m + 1, r, k - left.cnt);
        return cost;
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
            builder.append(cnt).append(",");
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
