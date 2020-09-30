package contest;

import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;

public class ThreeDigits {
    public String calculate(int X, int Y, int Z) {
        Modular mod = new Modular(Z * 1000);
        Power pow = new Power(mod);
        long exp = pow.pow(X, Y) * 1000L / Z;

        long inf = (long) 1e18;
        long actual = DigitUtils.limitPow(X, Y, inf);

        String ans = "";
        if (actual / (Z * 1000L) > 0) {
            ans += toString(exp / 1000, 1);
        }else{
            ans += exp / 1000;
        }

        ans += "." + toString(exp % 1000, 1);
        return ans;
    }


    public String toString(long x, int d) {
        if (d > 3) {
            return "";
        }
        return toString(x / 10, d + 1) + (x % 10);
    }
}
