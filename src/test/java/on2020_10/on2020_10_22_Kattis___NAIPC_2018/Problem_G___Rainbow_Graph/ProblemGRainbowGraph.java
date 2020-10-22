package on2020_10.on2020_10_22_Kattis___NAIPC_2018.Problem_G___Rainbow_Graph;



import template.algo.MatroidIndependentSet;
import template.algo.MaximumWeightMatroidIntersect;
import template.datastructure.DSU;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;

public class ProblemGRainbowGraph {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] ae = new int[2][m];
        int[][] be = new int[2][m];
        long[] w = new long[m];

        DSU ad = new DSU(n);
        DSU bd = new DSU(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            w[i] = in.readInt();
            char c = in.readChar();
            if (c != 'B') {
                ae[0][i] = u;
                ae[1][i] = v;
                ad.merge(u, v);
            }
            if (c != 'R') {
                be[0][i] = u;
                be[1][i] = v;
                bd.merge(u, v);
            }
        }

        long sum = Arrays.stream(w).sum();
        for (int i = 1; i < n; i++) {
            if (ad.find(i) != ad.find(0) ||
                    bd.find(i) != bd.find(0)) {
                for (int j = 0; j < m; j++) {
                    out.println(-1);
                }
                return;
            }
        }

        IntegerArrayList ans = new IntegerArrayList(m);
        MaximumWeightMatroidIntersect mwmi = new MaximumWeightMatroidIntersect(m, w);
        mwmi.setCallback(added -> {
            long x = 0;
            for (int i = 0; i < m; i++) {
                if (added[i]) {
                    x += w[i];
                }
            }
            ans.add((int) (sum - x));
            //debug.debug("added", Arrays.toString(added));
        });
        mwmi.intersect(MatroidIndependentSet.ofDeletionOnGraphRetainConnected(n, ae),
                MatroidIndependentSet.ofDeletionOnGraphRetainConnected(n, be));
        while (ans.size() < m) {
            ans.add(-1);
        }
        while (ans.size() > m) {
            ans.pop();
        }
        ans.reverse();
        for (int x : ans.toArray()) {
            out.println(x);
        }
    }
}
