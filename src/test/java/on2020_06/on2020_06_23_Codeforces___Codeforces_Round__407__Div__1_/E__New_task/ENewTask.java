package on2020_06.on2020_06_23_Codeforces___Codeforces_Round__407__Div__1_.E__New_task;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.ArrayIndex;
import template.utils.Debug;

import java.util.Arrays;

public class ENewTask {
    Modular mod = new Modular(1000000007);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.elapse("init");
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        IntegerDiscreteMap idm = new IntegerDiscreteMap(a.clone(), 0, a.length);
        for (int i = 0; i < n; i++) {
            a[i] = idm.rankOf(a[i]);
        }
        IntegerMultiWayStack valueStack = new IntegerMultiWayStack(idm.maxRank() + 1, n);
        for (int i = 0; i < n; i++) {
            valueStack.addLast(a[i], i);
        }
        int m = in.readInt();
        int[] tags = new int[m];
        int[][] events = new int[m][2];
        for (int i = 0; i < m; i++) {
            events[i][0] = in.readInt();
            events[i][1] = in.readInt() - 1;
        }
        IntegerMultiWayDeque eventStack = new IntegerMultiWayDeque(idm.maxRank() + 1, m);
        for (int i = 0; i < m; i++) {
            eventStack.addLast(a[events[i][1]], i);
        }
        debug.elapse("read data");

        Segment seg = new Segment(0, n);

        for (int i = 0; i <= idm.maxRank(); i++) {
            for (IntegerIterator iterator = valueStack.iterator(i); iterator.hasNext(); ) {
                int next = iterator.next();
                seg.update(next, next, 0, n, 1);
            }
            debug.elapse("add 1");
            int cur = seg.state.cnt[State.ai.indexOf(0, 4)];
            //debug.debug("seg.state.cnt", seg.state.cnt, State.ai);
            tags[0] = mod.plus(tags[0], cur);

            for (IntegerIterator iterator = eventStack.iterator(i); iterator.hasNext(); ) {
                int index = iterator.next();
                int how = events[index][0];
                int which = events[index][1];
                if (how == 1) {
                    //can't be used again
                    seg.update(which, which, 0, n, 0);
                } else {
                    seg.update(which, which, 0, n, 1);
                }

                int now = seg.state.cnt[State.ai.indexOf(0, 4)];
                int delta = now - cur;
                tags[index] = mod.plus(tags[index], delta);
                cur = now;
            }
            debug.elapse("handle event");

            for (IntegerIterator iterator = valueStack.iterator(i); iterator.hasNext(); ) {
                int next = iterator.next();
                seg.update(next, next, 0, n, 0);
            }
            debug.elapse("add 0");
        }

        for (int i = 1; i < m; i++) {
            tags[i] = mod.plus(tags[i - 1], tags[i]);
        }

        for (int i = 0; i < m; i++) {
            out.println(tags[i]);
        }

        debug.elapse("output");
    }

}

class State {
    static long mod = 1000000007;

    static ArrayIndex ai = new ArrayIndex(5, 5);
    static State buf = new State();
    int[] cnt = new int[ai.totalSize()];


    public void swap() {
        System.arraycopy(cnt, 0, buf.cnt, 0, cnt.length);
//        int[] tmp = cnt;
//        cnt = buf.cnt;
//        buf.cnt = tmp;
    }

    public void init(int x) {
        if (x == 0) {
            for (int i = 0; i < 5; i++) {
                cnt[ai.indexOf(i, i)] = 0;
            }
            cnt[ai.indexOf(0, 0)] = cnt[ai.indexOf(4, 4)] = 1;
        } else if (x == 1) {
            for (int i = 0; i < 5; i++) {
                cnt[ai.indexOf(i, i)] = 1;
            }
        }
    }

    public void mergeInto(State a, State b) {
        for (int i = 0; i < 5; i++) {
            for (int j = i; j < 5; j++) {
                long ans = a.cnt[ai.indexOf(i, j)] + b.cnt[ai.indexOf(i, j)];
                for (int k = i; k < j; k++) {
                    ans += (long) a.cnt[ai.indexOf(i, k)] * b.cnt[ai.indexOf(k + 1, j)] % mod;
                }
                cnt[ai.indexOf(i, j)] = (int) (ans % mod);
            }
        }
    }

    public static void mergeIntoLeft(State a, State b) {
        a.swap();
        a.mergeInto(buf, b);
    }

    @Override
    public String toString() {
        return Arrays.toString(cnt);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    State state = new State();

    private void modify(int x) {
        state.init(x);
    }

    public void pushUp() {
        state.mergeInto(left.state, right.state);
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
            modify(2);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, State ans) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            State.mergeIntoLeft(ans, state);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, ans);
        right.query(ll, rr, m + 1, r, ans);
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
