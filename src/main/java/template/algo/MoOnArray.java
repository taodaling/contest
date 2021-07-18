package template.algo;

import java.util.Arrays;
import java.util.Comparator;

public class MoOnArray {
    public static <Q extends Query> void solve(int rangeL, int rangeR, State<Q> state, Q[] qs) {
        if (qs.length == 0) {
            return;
        }
        int n = Math.max(rangeR - rangeL + 1, qs.length);
        int k = (int) Math.ceil(n / Math.sqrt(qs.length));
        solve(rangeL, rangeR, state, qs, k);
    }

    public static <Q extends Query> void solve(int rangeL, int rangeR, State<Q> state, Q[] qs, int k) {
        Arrays.sort(qs, (a, b) -> {
            int ans = a.left() / k - b.left() / k;
            if (ans == 0) {
                ans = a.right() - b.right();
            }
            return ans;
        });

        int l = rangeL;
        int r = rangeL - 1;
        for (Q q : qs) {
            int tl = q.left();
            int tr = q.right();
            while (l > tl) {
                l--;
                state.add(l);
            }
            while (r < tr) {
                r++;
                state.add(r);
            }
            while (l < tl) {
                state.remove(l);
                l++;
            }
            while (r > tr) {
                state.remove(r);
                r--;
            }
            state.answer(q);
        }
    }

    /**
     * O(qk+n^2/k+q\log_2q)
     *
     * @param rangeL
     * @param rangeR
     * @param state
     * @param qs
     * @param k
     * @param <Q>
     */
    public static <Q extends Query> void addOnlySolve(int rangeL, int rangeR, AddOnlyState<Q> state, Q[] qs, int k) {
        Arrays.sort(qs, Comparator.<Q>comparingInt(x -> x.left() / k).thenComparingInt(Q::right));

        //empty state
        state.save();

        //handle query in single block
        for (Q q : qs) {
            int L = q.left();
            int R = q.right();
            int Lb = L / k;
            int Rb = R / k;
            if (Rb != Lb) {
                continue;
            }
            for (int i = L; i <= R; i++) {
                state.add(i);
            }
            state.answer(q);
            for (int i = L; i <= R; i++) {
                state.remove(i);
            }
            state.rollback();
            state.save();
        }
        int l = Math.min(k - 1, rangeR);
        int r = l - 1;
        for (Q q : qs) {
            int L = q.left();
            int R = q.right();
            int Lb = L / k;
            int Rb = R / k;
            if (Rb == Lb) {
                continue;
            }
            int to = Math.min((Lb + 1) * k - 1, rangeR);
            if (l != to) {
                while (l <= r) {
                    state.remove(l++);
                }
                state.rollback();
                state.save();
                l = to;
                r = l - 1;
            }
            while (r < R) {
                r++;
                state.add(r);
            }
            state.save();
            while (l > L) {
                l--;
                state.add(l);
            }
            state.answer(q);

            while (l < to) {
                state.remove(l);
                l++;
            }
            state.rollback();
        }
    }

    /**
     * O(qk+n^2/k+q\log_2q)
     *
     * @param rangeL
     * @param rangeR
     * @param state
     * @param qs
     * @param k
     * @param <Q>
     */
    public static <Q extends Query> void removeOnlySolve(int rangeL, int rangeR, RemoveOnlyState<Q> state, Q[] qs, int k) {
        Arrays.sort(qs, Comparator.<Q>comparingInt(x -> x.left() / k).thenComparingInt(x -> -x.right()));

        //empty state
        state.save();

        //handle query in single block
        int l = rangeL;
        int r = rangeR;
        for (Q q : qs) {
            int L = q.left();
            int R = q.right();
            int Lb = L / k;
            int to = Math.max(Lb * k, rangeL);
            if (l != to) {
                while (r < rangeR) {
                    ++r;
                    state.add(r);
                }
                state.rollback();
                while (l < to) {
                    state.remove(l);
                    l++;
                }
                state.save();
            }
            while (r > R) {
                state.remove(r);
                r--;
            }
            state.save();
            while (l < L) {
                state.remove(l);
                l++;
            }
            state.answer(q);
            while (l > to) {
                l--;
                state.add(l);
            }
            state.rollback();
        }
    }

    public static <Q extends VersionQuery, M extends Modify> void solve(int rangeL, int rangeR, int now, ModifiableState<Q, M> state, Q[] qs, M[] ms) {
        if (qs.length == 0) {
            return;
        }
        int n = Math.max(Math.max(rangeR - rangeL + 1, qs.length), ms.length);
        int k = (int) Math.ceil(Math.pow(n, 2.0 / 3));
        solve(rangeL, rangeR, now, state, qs, ms, k);
    }

    public static <Q extends VersionQuery, M extends Modify> void solve(int rangeL, int rangeR, int now, ModifiableState<Q, M> state, Q[] qs, M[] ms, int k) {
        Arrays.sort(qs, (a, b) -> {
            int ans = a.left() / k - b.left() / k;
            if (ans == 0) {
                ans = (a.version() + 1) / k - (b.version() + 1) / k;
            }
            if (ans == 0) {
                ans = a.right() - b.right();
            }
            return ans;
        });

        int l = rangeL;
        int r = rangeL - 1;
        int v = now;
        for (Q q : qs) {
            int tl = q.left();
            int tr = q.right();
            int tv = q.version();
            while (l > tl) {
                l--;
                state.add(l);
            }
            while (r < tr) {
                r++;
                state.add(r);
            }
            while (l < tl) {
                state.remove(l);
                l++;
            }
            while (r > tr) {
                state.remove(r);
                r--;
            }
            while (v < tv) {
                v++;
                int index = ms[v].index();
                if (include(l, r, index)) {
                    state.remove(index);
                    state.apply(ms[v]);
                    state.add(index);
                } else {
                    state.apply(ms[v]);
                }
            }
            while (v > tv) {
                int index = ms[v].index();
                if (include(l, r, index)) {
                    state.remove(index);
                    state.revoke(ms[v]);
                    state.add(index);
                } else {
                    state.revoke(ms[v]);
                }
                v--;
            }
            state.answer(q);
        }
    }


    private static boolean include(int l, int r, int index) {
        return l <= index && index <= r;
    }

    public static interface Modify {
        int index();
    }

    public static interface Query {
        public int left();

        public int right();
    }

    public static interface VersionQuery extends Query {
        public int version();
    }

    public static interface State<Q extends Query> {
        public void answer(Q q);

        public void add(int i);

        public void remove(int i);
    }

    public static interface AddOnlyState<Q extends Query> extends State<Q> {
        public void save();

        public void rollback();

    }

    public static interface RemoveOnlyState<Q extends Query> extends State<Q> {
        public void save();

        public void rollback();

    }

    public static interface ModifiableState<Q extends VersionQuery, M extends Modify> extends State<Q> {
        public void apply(M m);

        public void revoke(M m);
    }
}
