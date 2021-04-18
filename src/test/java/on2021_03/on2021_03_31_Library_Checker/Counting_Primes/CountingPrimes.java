package on2021_03.on2021_03_31_Library_Checker.Counting_Primes;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.math.PrimeCounter;

public class CountingPrimes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();

        PrimeCounter pc = new PrimeCounter(new PrimeCounter.Function() {
            @Override
            public long f(long p) {
                return 1;
            }

            @Override
            public long sum(long n) {
                return n - 1;
            }
        });

        long ans = pc.get(n);
        out.println(ans);
    }
}
