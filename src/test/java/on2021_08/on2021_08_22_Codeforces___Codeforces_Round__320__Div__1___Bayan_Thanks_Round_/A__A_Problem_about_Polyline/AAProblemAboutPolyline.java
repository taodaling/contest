package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.A__A_Problem_about_Polyline;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.DoublePredicate;

public class AAProblemAboutPolyline {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        if (b > a) {
            out.println(-1);
            return;
        }
        long k = a / b;
        if (k * b == a && k % 2 == 1) {
            out.println(b);
            return;
        }
        DoublePredicate pred;
        long two = (a - b) / (2 * b) + 1;
        pred = m -> {
            double offset = a - two * m * 2;
            return -offset >= b;
        };


//        pred.test(1);
        double ans = BinarySearch.firstTrue(pred, b, 1e20, 1e-10, 1e-10);
        out.println(ans);
    }
}
