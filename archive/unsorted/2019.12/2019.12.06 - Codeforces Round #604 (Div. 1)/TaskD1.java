package contest;



import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Modular;

public class TaskD1 {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int n = s.length;
        int[] lcnt = new int[n];
        int[] rcnt = new int[n];
        int[] qcnt = new int[n];
        for (int i = 0; i < n; i++) {
            if (s[i] == '(') {
                lcnt[i] = 1;
            } else if (s[i] == ')') {
                rcnt[i] = 1;
            } else {
                qcnt[i] = 1;
            }
        }

        PreSum lps = new PreSum(lcnt);
        PreSum rps = new PreSum(rcnt);
        PreSum qps = new PreSum(qcnt);
        Composite comp = new Composite(1000000, mod);

        int[] geq = new int[n + 1];
        int[][] rGeq = new int[n + 1][n + 1];
        rGeq[n][0] = 1;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= n; j++) {
                if (s[i] == '(') {
                    rGeq[i][j] = rGeq[i + 1][j];
                } else if (s[i] == ')') {
                    rGeq[i][j] = j == 0 ? 0 : rGeq[i + 1][j - 1];
                } else {
                    rGeq[i][j] = rGeq[i + 1][j];
                    if (j > 0) {
                        rGeq[i][j] = mod.plus(rGeq[i][j], rGeq[i + 1][j - 1]);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = n - 1; j >= 0; j--) {
                rGeq[i][j] = mod.plus(rGeq[i][j + 1], rGeq[i][j]);
            }
        }

        for (int i = 0; i < n; i++) {
            if (s[i] == ')') {
                continue;
            }
            int leftCnt = (int) lps.intervalSum(0, i);
            int quesCnt = (int) qps.intervalSum(0, i);
            if (s[i] == '?') {
                leftCnt++;
                quesCnt--;
            }
            for (int j = 0; j <= quesCnt; j++) {
                geq[leftCnt + j] = mod.plus(geq[leftCnt + j],
                        mod.mul(comp.composite(quesCnt, j), rGeq[i + 1][leftCnt + j]));
            }
        }

        int ans = 0;
        for (int i = 1; i <= n; i++) {
            ans = mod.plus(geq[i], ans);
        }
        out.println(ans);
    }
}
