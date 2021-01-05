package contest;

import template.graph.MatrixTreeTheoremBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.utils.CompareUtils;

public class DC4 {
    int mod = 998244353;
    Factorial fact = new Factorial((int) 2e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int c = in.ri();
        int d = in.ri();
        int min = CompareUtils.minOf(a, b, c, d);
        int[] e = new int[]{a, b, c, d};
        long ans = 0;
        MatrixTreeTheoremBeta beta = new MatrixTreeTheoremBeta(5, mod, true);
        for (int i = 0; i <= min; i++) {
            boolean skip = false;
            for (int j = 0; j < 4; j++) {
                if ((e[j] - i) % 2 != 0) {
                    skip = true;
                }
            }
            if (skip) {
                continue;
            }
            beta.init();
            long factor = 1;
            for (int j = 0; j < 4; j++) {
                int x = i + (e[j] - i) / 2;
                int y = e[j] - x;
                factor = factor * fact.invFact(x) % mod * fact.invFact(y) % mod;
                beta.addDirectedEdge(j, (j + 1) % 4, x);
                beta.addDirectedEdge((j + 1) % 4, j, y);
            }
            beta.addDirectedEdge(4, 0, 1);
            beta.addDirectedEdge(0, 4, 1);
            ans += beta.countEulerTrace(fact) * factor % mod;
            if (i > 0) {
                beta.init();
                for (int j = 0; j < 4; j++) {
                    int x = i + (e[j] - i) / 2;
                    int y = e[j] - x;
                    beta.addDirectedEdge((j + 1) % 4, j, x);
                    beta.addDirectedEdge(j, (j + 1) % 4, y);
                }
                beta.addDirectedEdge(4, 0, 1);
                beta.addDirectedEdge(0, 4, 1);
                ans += beta.countEulerTrace(fact) * factor % mod;
            }
        }
        ans %= mod;
        out.println(ans);
    }
}
