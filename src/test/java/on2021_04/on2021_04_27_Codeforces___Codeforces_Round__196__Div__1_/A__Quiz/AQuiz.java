package on2021_04.on2021_04_27_Codeforces___Codeforces_Round__196__Div__1_.A__Quiz;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;

public class AQuiz {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        long mayPut = (long) (n - m) * (k - 1);
        if (mayPut >= m) {
            out.println(m);
            return;
        }

        long ans = dup(m  - mayPut, k);
        ans += mayPut;
        ans %= mod;
        out.println(ans);
    }

    int mod = (int) 1e9 + 9;
    Power pow = new Power(mod);

    public long dup(long n, long k) {
        long r = n % k;
        n -= r;
        long m = n / k;
        long ans = k * (pow.pow(2, m + 1) - 2);
        ans += r;
        ans = DigitUtils.mod(ans, mod);
        return ans;
    }
}
