package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting4;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.utils.CompareUtils;

public class EGoshaIsHunting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = in.readInt();
        b = in.readInt();
        p = new double[n];
        u = new double[n];
        in.populate(p);
        in.populate(u);

        solve1();
        out.println(ans);
    }

    public void solve1() {
        double l = 0;
        double r = 1;
        for (int i = 0; i < 60; i++) {
            double m = (l + r) / 2;
            solve2(m);
            if (time1 > a) {
                l = m;
            } else if (time1 < a) {
                r = m;
            } else {
                break;
            }
        }
    }

    public void solve2(double x) {
        double l = 0;
        double r = 1;
        for (int i = 0; i < 60; i++) {
            double m = (l + r) / 2;
            solve3(x, m);
            if (time2 > b) {
                l = m;
            } else if (time2 < b) {
                r = m;
            } else {
                break;
            }
        }
    }

    double ans;
    int n;
    int a;
    int b;
    int time1;
    int time2;
    double[] p;
    double[] u;

    public void solve3(double pCost, double uCost) {
        KahanSummation sum = new KahanSummation();
        time1 = 0;
        time2 = 0;
        for (int i = 0; i < n; i++) {
            double contrib = 0;
            contrib = Math.max(contrib, p[i] - pCost);
            contrib = Math.max(contrib,u[i] - uCost);
            contrib = Math.max(contrib, p[i] + u[i] - p[i] * u[i] - pCost - uCost);
            if (contrib == p[i] - pCost) {
                time1++;
            } else if (contrib == u[i] - uCost) {
                time2++;
            } else if (contrib == p[i] + u[i] - p[i] * u[i] - pCost - uCost) {
                time1++;
                time2++;
            }
            sum.add(contrib);
        }
        ans = sum.sum() + pCost * a + uCost * b;
    }
}
