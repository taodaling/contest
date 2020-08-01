package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting2;



import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;

public class EGoshaIsHunting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        double[] p = new double[n];
        double[] u = new double[n];
        in.populate(p);
        in.populate(u);

        double prec = 1e-10;
        double l = 0;
        double r = 2;
        double x = 0;
        double y = 0;
        while (r - l > prec) {
            x = (r + l) / 2;
            double ll = 0;
            double rr = 2;
            while (rr - ll > prec) {
                y = (ll + rr) / 2;
                if (dp(p, u, x, y).u > b) {
                    ll = y;
                } else {
                    rr = y;
                }
            }
            if (dp(p, u, x, y).p > a) {
                l = x;
            } else {
                r = x;
            }
        }

        Result result = dp(p, u, x, y);
        double ans = result.profit + x * a + y * b;
        out.println(ans);
    }

    public Result dp(double[] p, double[] u, double pCost, double uCost) {
        int n = p.length;
        KahanSummation sum = new KahanSummation();
        int time1 = 0;
        int time2 = 0;
        double prec = 1e-8;
        for (int i = 0; i < n; i++) {
            double contrib = 0;
            int t1 = 0;
            int t2 = 0;
            if (contrib + prec < p[i] - pCost) {
                contrib = p[i] - pCost;
                t1 = 1;
                t2 = 0;
            }

            if (contrib + prec < u[i] - uCost) {
                contrib = u[i] - uCost;
                t1 = 0;
                t2 = 1;
            }

            if (contrib + prec < p[i] + u[i] - p[i] * u[i] - pCost - uCost) {
                contrib = p[i] + u[i] - p[i] * u[i] - pCost - uCost;
                t1 = 1;
                t2 = 1;
            }
            sum.add(contrib);
            time1 += t1;
            time2 += t2;
        }
        return new Result(sum.sum(), time1, time2);
    }
}


class Result {
    double profit;
    int p;
    int u;

    public Result(double profit, int p, int u) {
        this.profit = profit;
        this.p = p;
        this.u = u;
    }

}