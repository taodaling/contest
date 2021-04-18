package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.C___Multiple_Sequences;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class CMultipleSequences {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);

    public long seq(int n, int len) {
        //0...n
        //x1+x2+...+xlen=n
        return comb.combination(n + len - 1, len - 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime(m);
        long ans = 0;
        for (int i = 1; i <= m; i++) {
            long way = 1;
            for (IntegerIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                int p = iterator.next();
                int log = 0;
                int x = i;
                while (x % p == 0) {
                    x /= p;
                    log++;
                }
                way = way * seq(log, n) % mod;
            }
            ans += way;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
