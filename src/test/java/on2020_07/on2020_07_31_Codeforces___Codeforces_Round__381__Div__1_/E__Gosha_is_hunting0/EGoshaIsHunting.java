package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting0;



import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;

import javax.annotation.Resource;

public class EGoshaIsHunting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        double[] p = new double[n];
        double[] u = new double[n];
        in.populate(p);
        in.populate(u);

        double prec = 1e-12;
        double range = 1000;
        DoubleBinarySearch dbs = new DoubleBinarySearch(prec, prec) {

            public boolean check(double x) {
                DoubleBinarySearch inner = new DoubleBinarySearch(prec, prec) {

                    public boolean check(double y) {
                        Result result = dp(p, u, x, y);
                        return result.u <= b;
                    }
                };
                double y = inner.binarySearch(-range, range);
                Result result = dp(p, u, x, y);
                return result.p <= a;
            }
        };

        double x = dbs.binarySearch(-range, range);
        DoubleBinarySearch inner = new DoubleBinarySearch(prec, prec) {

            public boolean check(double y) {
                Result result = dp(p, u, x, y);
                return result.u <= b;
            }
        };
        double y = inner.binarySearch(-range, range);
        Result result = dp(p, u, x, y);

        double ans = result.profit + x * a + y * b;
        out.println(ans);
    }

    public Result dp(double[] p, double[] u, double pCost, double uCost) {
        int n = p.length;
        KahanSummation sum = new KahanSummation();
        int time1 = 0;
        int time2 = 0;
        for (int i = 0; i < n; i++) {
            double contrib = 0;
            int t1 = 0;
            int t2 = 0;
            if (contrib < p[i] - pCost) {
                contrib = p[i] - pCost;
                t1 = 1;
                t2 = 0;
            }
            if (contrib < u[i] - uCost) {
                contrib = u[i] - uCost;
                t1 = 0;
                t2 = 1;
            }
            if (contrib < p[i] + u[i] - p[i] * u[i] - pCost - uCost) {
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