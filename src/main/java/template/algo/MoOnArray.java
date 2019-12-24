package template.algo;

import java.util.Arrays;

public class MoOnArray {
    public static <T, Q extends MoOnArray.Query> void handle(T[] data, Q[] queries, Handler<T, Q> handler) {
        if(queries.length == 0 || data.length == 0){
            return;
        }

        int n = data.length;
        int blockSize = (int) Math.ceil(Math.sqrt(n));
        Arrays.sort(queries, (a, b) -> {
            int ans = a.getL() / blockSize - b.getL() / blockSize;
            if (ans == 0) {
                ans = a.getR() - b.getR();
            }
            return ans;
        });

        int l = queries[0].getL();
        int r = l - 1;
        for (Q q : queries) {
            int ll = q.getL();
            int rr = q.getR();
            while (ll < l) {
                l--;
                handler.add(l, data[l]);
            }
            while (rr > r) {
                r++;
                handler.add(r, data[r]);
            }
            while (ll > l) {
                handler.remove(l, data[l]);
                l++;
            }
            while (rr < r) {
                handler.remove(r, data[r]);
                r--;
            }
            handler.answer(q);
        }
    }

    public static <Q extends MoOnArray.Query> void handle(int[] data, Q[] queries, IntHandler<Q> handler) {
        if(queries.length == 0 || data.length == 0){
            return;
        }

        int n = data.length;
        int blockSize = (int) Math.ceil(Math.sqrt(n));
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
}
