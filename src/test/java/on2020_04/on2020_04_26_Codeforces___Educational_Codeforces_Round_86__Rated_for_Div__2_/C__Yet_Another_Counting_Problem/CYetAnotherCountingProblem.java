package on2020_04.on2020_04_26_Codeforces___Educational_Codeforces_Round_86__Rated_for_Div__2_.C__Yet_Another_Counting_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LCMs;

public class CYetAnotherCountingProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int c = (int) LCMs.lcm(a, b);
        int q = in.readInt();
        while (q-- > 0) {
            long l = in.readLong();
            long r = in.readLong();

            long ans = solve(a, b, c, r) - solve(a, b, c, l - 1);
            out.append(r - l + 1 - ans).append(' ');
        }
        out.println();
    }

    public long solve(long a, long b, long c, long n) {
        long block = n / c;
        long ans = block * a;
        long remain = n % c;
        if (remain >= a) {
            ans += a;
        } else {
            ans += remain + 1;
        }
        return ans;
    }
}
