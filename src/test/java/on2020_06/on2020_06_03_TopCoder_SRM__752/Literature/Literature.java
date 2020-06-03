package on2020_06.on2020_06_03_TopCoder_SRM__752.Literature;



import template.utils.ArrayIndex;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class Literature {
    double[] exp;
    ArrayIndex ai;
    int n;

   // Debug debug = new Debug(true);

    public double exp(int i, int j, int round) {
        round %= 3;
        if (exp[ai.indexOf(i, j, round)] == -2) {
            if (i == n || j == n) {
                return exp[ai.indexOf(i, j, round)] = 0;
            }
            //first people first

            if (round == 0) {
                return exp[ai.indexOf(i, j, round)] = 1 + exp(i, j, round + 1);
            }
            if (round == 1) {
                double p1 = (n - j) / (2.0 * n);
                double p2 = (n - i) / (2.0 * n) * (1 - p1);
                exp[ai.indexOf(i, j, round)] = (p1 * (exp(i, j + 1, round + 1) + 1)
                        + p2 * (exp(i + 1, j, round + 2) + 2) + (1 - p1 - p2) * 3)
                        / (p1 + p2);
            } else {
                double p1 = (n - i) / (2.0 * n);
                double p2 = (n - j) / (2.0 * n) * (1 - p1);
                exp[ai.indexOf(i, j, round)] = (p1 * (exp(i + 1, j, round + 1) + 1)
                        + p2 * (exp(i, j + 1, round + 3) + 3) + (1 - p1 - p2) * 3)
                        / (p1 + p2);
            }
        }

        return exp[ai.indexOf(i, j, round)];
    }

    public double expectation(int n, int[] Teja, int[] history) {
        this.n = n;
        ai = new ArrayIndex(n + 1, n + 1, 3);
        exp = new double[ai.totalSize()];
        SequenceUtils.deepFill(exp, -2D);

        int[] belong = new int[n * 3 + 1];
        int[] cnts = new int[3];
        SequenceUtils.deepFill(belong, -1);
        for (int t : Teja) {
            belong[t] = 0;
        }
        for (int i = 0; i < history.length; i++) {
            if (i % 3 == 0) {
                continue;
            }
            if (i % 3 == 1 && belong[history[i]] == -1) {
                belong[history[i]] = 2;
                cnts[2]++;
            }
            if (i % 3 == 2 && belong[history[i]] == -1) {
                belong[history[i]] = 1;
                cnts[1]++;
            }
            if (cnts[1] == n || cnts[2] == n) {
                return i + 1;
            }
        }
       // debug.debug("belong", belong);
       // debug.debug("cnts", cnts);

        double ans = history.length + exp(cnts[1], cnts[2], history.length);
        return ans;
    }
}
