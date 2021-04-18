package on2021_03.on2021_03_31_Luogu.P1835_____;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PrimeCounter;

public class P1835 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long l = in.rl();
        long r = in.rl();
        PrimeCounter pc = new PrimeCounter(new PrimeCounter.Function() {
            @Override
            public long f(long p) {
                return 1;
            }

            @Override
            public long sum(long n) {
                return Math.max(n - 1, 0);
            }
        });
        out.println(pc.get(r) - pc.get(l - 1));
    }
}
