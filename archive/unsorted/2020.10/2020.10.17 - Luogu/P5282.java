package contest;

import template.io.FastInput;
import template.polynomial.IntPolyFFT;
import template.problem.BigFactorial;

import java.io.PrintWriter;

public class P5282 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int p = in.readInt();

        BigFactorial factorial = new BigFactorial(p, new IntPolyFFT(p));
        int ans = factorial.solve(n);
        out.println(ans);
    }
}
