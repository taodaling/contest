package contest;

import template.math.Combination;
import template.math.Factorial;
import template.math.Power;

import java.util.Arrays;

public class ExpectedValue {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial(10000, mod);
    Combination comb = new Combination(fact);
    long[] f;
    Power pow = new Power(mod);

    public long f(int n) {
        if (n <= 1) {
            return n == 0 ? 1 : 0;
        }
        if (f[n] == -1) {
            long ans = 0;
            for (int i = 2; i <= n; i++) {
                ans += f(n - i) * comb.combination(n - 1, i - 1) % mod * fact.fact(i - 1) % mod;
            }
            f[n] = ans % mod;
        }
        return f[n];
    }

    public int solve(int N) {
        f = new long[N + 1];
        Arrays.fill(f, -1);
        long numOfCircle = 0;
        int invFn = pow.inverse((int)f(N));
        for (int i = 2; i <= N; i++) {
            numOfCircle += comb.combination(N, i) * (long) fact.fact(i - 1) % mod * f(N - i) % mod * invFn % mod;
        }
        long ans = N - numOfCircle;
        ans %= mod;
        if(ans < 0){
            ans += mod;
        }
        return (int)ans;
    }
}
