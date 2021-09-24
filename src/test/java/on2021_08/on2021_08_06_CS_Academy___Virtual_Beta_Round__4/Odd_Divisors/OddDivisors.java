package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Odd_Divisors;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class OddDivisors {
    public long solve(long n) {
        long ans = IntMath.sumOfInterval(1, n);
        while (n > 0) {
            n /= 2;
            ans -= IntMath.sumOfInterval(1, n);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        long ans = solve(b) - solve(a - 1);
        out.println(ans);
    }
}
