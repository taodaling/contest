package on2020_10.on2020_10_17_Codeforces___Codeforces_Raif_Round_1__Div__1___Div__2_.F__Fruit_Sequences;



import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FFruitSequences {

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] seq = new int[n];
        for (int i = 0; i < n; i++) {
            seq[i] = in.readChar() - '0';
        }


        long extra = 0;
        List<Event> events = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (seq[i] == 0) {
                continue;
            }
            int j = i;
            while (j + 1 < n && seq[j + 1] == 1) {
                j++;
            }
            if (j > i) {
                int internal = j - i + 1 - 2;
                for (int k = 1; k <= internal; k++) {
                    extra += (long) k * (internal - k + 1);
                }
            }
            for (int k = i; k <= j; k++) {
                events.add(new Event(i, k - i + 1, k, n - 1));
            }
            for (int k = j; k >= i; k--) {
                events.add(new Event(k, j - k + 1, j, n - 1));
            }

            i = j;
        }
        events.sort((a, b) -> Integer.compare(a.l, b.l));
        Range2DequeAdapter<Event> dq = new Range2DequeAdapter<>(i -> events.get(i), 0, events.size() - 1);
        SegmentBeat sb = new SegmentBeat(0, n - 1, i -> 0);

        long sum = 0;
        for (int i = n - 1; i >= 0; i--) {
            while (!dq.isEmpty() && dq.peekLast().l >= i) {
                Event event = dq.removeLast();
                sb.updateMin(event.lo, event.hi, 0, n - 1, -event.profit);
            }
            long cnt = sb.querySum(i, n - 1, 0, n - 1);
            sum += cnt;
        }
        out.println(-sum + extra);
    }
}


class Event {
    int l;
    int profit;
    int lo;
    int hi;


    public Event(int l, int profit, int lo, int hi) {
        this.l = l;
        this.profit = profit;
        this.lo = lo;
        this.hi = hi;
    }
}

class SegmentBeat implements Cloneable {
    private SegmentBeat left;
    private SegmentBeat right;
    private long first;
    private long second;
    private int firstCnt;
    private static long inf = (long) 2e18;
    private long sum;

    private void setMin(long x) {
        if (first <= x) {
            return;
        }
        sum -= (first - x) * firstCnt;
        first = x;
    }

    public void pushUp() {
        first = Math.max(left.first, right.first);
        second = Math.max(left.first == first ? left.second : left.first, right.first == first ? right.second : right.first);
        firstCnt = (left.first == first ? left.firstCnt : 0) + (right.first == first ? right.firstCnt : 0);
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        left.setMin(first);
        right.setMin(first);
    }

    public SegmentBeat(int l, int r, IntToLongFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new SegmentBeat(l, m, func);
            right = new SegmentBeat(m + 1, r, func);
            pushUp();
        } else {
            sum = first = func.apply(l);
            second = -inf;
            firstCnt = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateMin(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (first <= x) {
                return;
            }
            if (second < x) {
                setMin(x);
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateMin(ll, rr, l, m, x);
        right.updateMin(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long querySum(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.querySum(ll, rr, l, m) +
                right.querySum(ll, rr, m + 1, r);
    }

    public long queryMax(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -inf;
        }
        if (covered(ll, rr, l, r)) {
            return first;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }

    private SegmentBeat deepClone() {
        SegmentBeat seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected SegmentBeat clone() {
        try {
            return (SegmentBeat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(sum).append(",");
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
