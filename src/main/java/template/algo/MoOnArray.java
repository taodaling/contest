package template.algo;

import java.util.Arrays;

public class MoOnArray {
    public static <Q extends Query> void solve(int rangeL, int rangeR, State<Q> state, Q[] qs) {
        if (qs.length == 0) {
            return;
        }
        int n = Math.max(rangeR - rangeL + 1, qs.length);
        int k = (int) Math.ceil(Math.sqrt(n));
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

    public static interface ModifiableState<Q extends VersionQuery, M extends Modify> extends State<Q> {
        public void apply(M m);

        public void revoke(M m);
    }
}
