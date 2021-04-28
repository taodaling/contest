package on2021_04.on2021_04_25_Codeforces___Codeforces_Round__200__Div__1_.A__Rational_Resistance;



import template.io.FastInput;
import template.io.FastOutput;

public class ARationalResistance {
    public long solve(long a, long b) {
        if (b == 0) {
            return 0;
        }
        long ans = a / b;
        a %= b;
        if (a > 0) {
            ans += solve(a, b % a) + b / a;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long ans = solve(a, b);
        out.println(ans);
    }
}
