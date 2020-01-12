package template.algo;

import java.util.Arrays;

public class MoOnArray {

    public static <T, Q extends Query> void handle(T[] data, Q[] queries, Handler<T, Q> handler) {
        int n = data.length;
        int blockSize = Math.max(1, (int) Math.ceil(Math.sqrt(queries.length)));
        handle(data, queries, handler, blockSize);
    }

    public static <T, Q extends Query> void handle(T[] data, Q[] queries, Handler<T, Q> handler, int blockSize) {
        if (queries.length == 0 || data.length == 0) {
            return;
        }

        Arrays.sort(queries, (a, b) -> {
            int ans = a.getL() / blockSize - b.getL() / blockSize;
            if (ans == 0) {
                ans = a.getR() - b.getR();
            }
            return ans;
        });

        int ll = queries[0].getL();
        int rr = ll - 1;
        for (Q q : queries) {
            int l = q.getL();
            int r = q.getR();
            while (l < ll) {
                ll--;
                handler.add(ll, data[ll]);
            }
            while (r > rr) {
                rr++;
                handler.add(rr, data[rr]);
            }
            while (l > ll) {
                handler.remove(ll, data[ll]);
                ll++;
            }
            while (r < rr) {
                handler.remove(rr, data[rr]);
                rr--;
            }
            handler.answer(q);
        }
    }

    public static <Q extends Query> void handle(int[] data, Q[] queries, IntHandler<Q> handler) {
        int n = data.length;
        int blockSize = Math.max(1, (int) Math.ceil(Math.sqrt(queries.length)));
        handle(data, queries, handler, blockSize);
    }

    public static <Q extends Query> void handle(int[] data, Q[] queries, IntHandler<Q> handler, int blockSize) {
        if (queries.length == 0 || data.length == 0) {
            return;
        }

        Arrays.sort(queries, (a, b) -> {
            int ans = a.getL() / blockSize - b.getL() / blockSize;
            if (ans == 0) {
                ans = a.getR() - b.getR();
            }
            return ans;
        });

        int ll = queries[0].getL();
        int rr = ll - 1;
        for (Q q : queries) {
            int l = q.getL();
            int r = q.getR();
            while (l < ll) {
                ll--;
                handler.add(ll, data[ll]);
            }
            while (r > rr) {
                rr++;
                handler.add(rr, data[rr]);
            }
            while (l > ll) {
                handler.remove(ll, data[ll]);
                ll++;
            }
            while (r < rr) {
                handler.remove(rr, data[rr]);
                rr--;
            }
            handler.answer(q);
        }
    }

    public static <T, Q extends VersionQuery> void handle(T[] data, Modify<T>[] modifies, Q[] queries, Handler<T, Q> handler) {
        int n = data.length;
        int blockSize = (int) Math.ceil(Math.pow(n, 2.0 / 3));
        handle(data, modifies, queries, handler, blockSize);
    }

    public static <T, Q extends VersionQuery> void handle(T[] data, Modify<T>[] modifies, Q[] queries, Handler<T, Q> handler, int blockSize) {
        if (queries.length == 0 || data.length == 0) {
            return;
        }

        Arrays.sort(queries, (a, b) -> {
            int ans = a.getL() / blockSize - b.getL() / blockSize;
            if (ans == 0) {
                ans = a.getVersion() / blockSize - b.getVersion() / blockSize;
            }
            if (ans == 0) {
                ans = a.getR() / blockSize - b.getR() / blockSize;
            }
            return ans;
        });

        int v = 0;
        int l = queries[0].getL();
        int r = l - 1;
        for (Q q : queries) {
            int ll = q.getL();
            int rr = q.getR();
            int vv = q.getVersion();

            while (v < vv) {
                modifies[v].apply(data, handler, l, r);
                v++;
            }
            while (v > vv) {
                v--;
                modifies[v].revoke(data, handler, l, r);
            }
            while (l > ll) {
                l--;
                handler.add(l, data[l]);
            }
            while (r < rr) {
                r++;
                handler.add(r, data[r]);
            }
            while (l < ll) {
                handler.remove(l, data[l]);
                l++;
            }
            while (r > rr) {
                handler.remove(r, data[r]);
                r--;
            }
            handler.answer(q);
        }
    }

    public static <Q extends VersionQuery> void handle(int[] data, IntModify[] modifies, Q[] queries, IntHandler<Q> handler) {
        int n = data.length;
        int blockSize = (int) Math.ceil(Math.pow(n, 2.0 / 3));
        handle(data, modifies, queries, handler, blockSize);
    }

    public static <Q extends VersionQuery> void handle(int[] data, IntModify[] modifies, Q[] queries, IntHandler<Q> handler, int blockSize) {
        if (queries.length == 0 || data.length == 0) {
            return;
        }

        Arrays.sort(queries, (a, b) -> {
            int ans = a.getL() / blockSize - b.getL() / blockSize;
            if (ans == 0) {
                ans = a.getVersion() / blockSize - b.getVersion() / blockSize;
            }
            if (ans == 0) {
                ans = a.getR() / blockSize - b.getR() / blockSize;
            }
            return ans;
        });

        int v = 0;
        int l = queries[0].getL();
        int r = l - 1;
        for (Q q : queries) {
            int ll = q.getL();
            int rr = q.getR();
            int vv = q.getVersion();

            while (v < vv) {
                modifies[v].apply(data, handler, l, r);
                v++;
            }
            while (v > vv) {
                v--;
                modifies[v].revoke(data, handler, l, r);
            }
            while (l > ll) {
                l--;
                handler.add(l, data[l]);
            }
            while (r < rr) {
                r++;
                handler.add(r, data[r]);
            }
            while (l < ll) {
                handler.remove(l, data[l]);
                l++;
            }
            while (r > rr) {
                handler.remove(r, data[r]);
                r--;
            }
            handler.answer(q);
        }
    }

    public interface Query {
        int getL();

        int getR();
    }

    public interface Handler<T, Q> {
        void add(int i, T x);

        void remove(int i, T x);

        void answer(Q q);
    }

    public interface IntHandler<Q> {
        void add(int i, int x);

        void remove(int i, int x);

        void answer(Q q);
    }

    public interface VersionQuery extends Query {
        int getVersion();
    }

    public interface Modify<T> {
        <Q extends VersionQuery> void apply(T[] data, Handler<T, Q> handler, int l, int r);

        <Q extends VersionQuery> void revoke(T[] data, Handler<T, Q> handler, int l, int r);
    }

    public interface IntModify {
        <Q extends VersionQuery> void apply(int[] data, IntHandler<Q> handler, int l, int r);

        <Q extends VersionQuery> void revoke(int[] data, IntHandler<Q> handler, int l, int r);
    }
}
