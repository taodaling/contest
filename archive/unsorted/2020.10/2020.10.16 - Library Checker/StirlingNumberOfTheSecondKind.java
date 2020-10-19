package contest;

import template.io.FastInput;
import template.math.Factorial;
import template.math.SecondStirlingNumber;
import template.polynomial.IntPolyNTT;

import java.io.PrintWriter;

public class StirlingNumberOfTheSecondKind {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        SecondStirlingNumber number = new SecondStirlingNumber(new Factorial(n, mod),
                n, new IntPolyNTT(mod));
        for(int i = 0; i <= n; i++){
            out.println(number.stirling(i));
        }
    }
}
