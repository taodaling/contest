package contest;

import template.io.FastInput;
import template.math.Factorial;
import template.math.FirstStirlingNumber;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;

import java.io.PrintWriter;

public class StirlingNumberOfTheFirstKind {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        FirstStirlingNumber number = new FirstStirlingNumber(new IntPolyNTT(mod),
                new Factorial(n, mod), n);
        for (int i = 0; i <= n; i++) {
            out.println(number.getSign(i));
        }
    }
}
