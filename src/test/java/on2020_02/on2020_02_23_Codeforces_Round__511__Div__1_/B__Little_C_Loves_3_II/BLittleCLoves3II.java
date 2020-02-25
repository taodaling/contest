package on2020_02.on2020_02_23_Codeforces_Round__511__Div__1_.B__Little_C_Loves_3_II;



import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;

public class BLittleCLoves3II {
    int n;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();

        long ans = 0;
        int remain = 30;
        if (n > remain) {
            int extra = n - remain;
            ans += (long) (extra - extra % 6) * m;
            n -= extra - extra % 6;
        }
        if(m > remain) {
            int extra = m - remain;
            ans += (long) (extra - extra % 6) * n;
            m -= extra - extra % 6;
        }

        KMAlgo km = new KMAlgo(n * m, n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if ((i + j) % 2 != 0) {
                    continue;
                }
                for (int k = 0; k < n; k++) {
                    for (int t = 0; t < m; t++) {
                        if ((k + t) % 2 != 1) {
                            continue;
                        }
                        if (Math.abs(i - k) + Math.abs(j - t) == 3) {
                            km.addEdge(cellId(i, j), cellId(k, t), true);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (km.matchLeft(cellId(i, j))) {
                    ans += 2;
                }
            }
        }

        out.println(ans);
    }

    int cellId(int i, int j) {
        return i * m + j;
    }
}
