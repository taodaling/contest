package on2020_06.on2020_06_27_Library_Checker.Factorize;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.LongList;

import java.util.Set;

public class Factorize {
    public void solve(int testNumber, FastInput in, FastOutput out) {
//        List<BigInteger> bp = new ArrayList<>();
//        BigPollardRho.factor(BigInteger.valueOf(4), bp);

        long x = in.readLong();
        Set<Long> bigPrimes = LongPollardRho.findAllFactors(x);
        LongList ans = new LongList(60);
        for (long factor : bigPrimes) {
            while (x % factor == 0) {
                x /= factor;
                ans.add(factor);
            }
        }

        ans.sort();
        out.append(ans.size()).append(' ');
        for (int i = 0; i < ans.size(); i++) {
            out.append(ans.get(i)).append(' ');
        }
        out.println();
    }
}
