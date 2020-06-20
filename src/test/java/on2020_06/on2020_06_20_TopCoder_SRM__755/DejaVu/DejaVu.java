package on2020_06.on2020_06_20_TopCoder_SRM__755.DejaVu;



import template.datastructure.DiscreteMap;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public class DejaVu {
    public int mostDejaVus(int N, int seed, int R) {
        long[] A = new long[N];
        A[0] = seed;
        for (int i = 1; i <= N - 1; i++) {
            A[i] = (A[i - 1] * 1664525 + 1013904223) % 4294967296L;
        }
        int[] M = new int[N];
        for (int i = 0; i <= N - 1; i++) {
            M[i] = (int) (A[i] % R);
        }
        IntegerDiscreteMap idm = new IntegerDiscreteMap(M.clone(), 0, N);
        for (int i = 0; i < N; i++) {
            M[i] = idm.rankOf(M[i]);
        }

        int[] next = new int[N + 1];
        next[N] = N;
        int[] registries = new int[N];
        Arrays.fill(registries, N);
        for (int i = N - 1; i >= 0; i--) {
            next[i] = registries[M[i]];
            registries[M[i]] = i;
        }
        boolean[] visited = new boolean[N];
        int[] state = new int[N + 1];
        for (int i = 0; i < N; i++) {
            if (visited[M[i]]) {
                continue;
            }
            visited[M[i]] = true;
            int m1 = i;
            int m2 = next[m1];
            int m3 = next[m2];
            state[m2] = 1;
            state[m3] = -1;
        }

        Segment seg = new Segment(0, N - 1, i -> state[i]);
        State query = new State();

        int ans = 0;
        for (int i = 0; i < N; i++) {
            query.reset(0);
            seg.query(i, N - 1, 0, N - 1, query);
            ans = Math.max(ans, query.prefix);

            int m1 = i;
            int m2 = next[m1];
            int m3 = next[m2];
            int m4 = next[m3];

            state[m2] = 0;
            state[m3] = 1;
            state[m4] = -1;

            seg.update(m2, m2, 0, N - 1, state[m2]);
            seg.update(m3, m3, 0, N - 1, state[m3]);
            seg.update(m4, m4, 0, N - 1, state[m4]);
        }

        return ans;
    }
}

class State {
    int sum;
    int prefix;
    static State buf = new State();

    public void copy(State state) {
        sum = state.sum;
        prefix = state.prefix;
    }

    public void reset(int x) {
        sum = x;
        prefix = Math.max(0, x);
    }

    public void mergeInto(State a, State b) {
        sum = a.sum + b.sum;
        prefix = Math.max(a.prefix, a.sum + b.prefix);
    }

    public static void mergeIntoLeft(State a, State b) {
        buf.copy(a);
        a.mergeInto(buf, b);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    State state = new State();


    private void modify(int x) {
        state.reset(x);
    }

    public void pushUp() {
        state.mergeInto(left.state, right.state);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            modify(func.apply(l));
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

    public void query(int ll, int rr, int l, int r, State query) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            State.mergeIntoLeft(query, state);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, query);
        right.query(ll, rr, m + 1, r, query);
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
