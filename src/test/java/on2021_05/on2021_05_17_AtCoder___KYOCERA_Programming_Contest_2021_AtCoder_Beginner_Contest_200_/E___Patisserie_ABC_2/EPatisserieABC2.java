package on2021_05.on2021_05_17_AtCoder___KYOCERA_Programming_Contest_2021_AtCoder_Beginner_Contest_200_.E___Patisserie_ABC_2;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigCombination;
import template.math.IntMath;
import template.primitve.generated.datastructure.LongPreSum;

import java.math.BigInteger;
import java.util.function.LongPredicate;

public class EPatisserieABC2 {

    public BigInteger way(long m, int k, boolean leq) {
        m -= k;
        BigInteger ans = BigInteger.ZERO;
        for (int i = 0; i <= k; i++) {
            long remain = m - n * i;
            if (remain < 0) {
                continue;
            }
            int choice = k + (leq ? 1 : 0);
            BigInteger cand = BigCombination.combination(remain + choice - 1, choice - 1)
                    .multiply(BigCombination.combination(k, i));
            if (i % 2 == 1) {
                cand = cand.negate();
            }
            ans = ans.add(cand);
        }
        return ans;
    }

    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        long k = in.rl();
        long finalK = k;
        LongPredicate first = x -> {
            return way(x, 3, true).compareTo(BigInteger.valueOf(finalK)) >= 0;
        };
        long m = BinarySearch.firstTrue(first, 3, 3 * n);
        k -= way(m - 1, 3, true).longValueExact();
        long finalK1 = k;
        LongPredicate second = x -> {
            return way(m - 1, 2, true).subtract(way(m - x - 1, 2, true)).compareTo(BigInteger.valueOf(finalK1)) >= 0;
        };
        long i = BinarySearch.firstTrue(second, 1, n);
        k -= way(m - 1, 2, true).subtract(way(m - (i - 1) - 1, 2, true)).longValueExact();
        long jMin = Math.max(m - i - n, 1);
        long j = jMin + k - 1;
        long t = m - i - j;


        out.append(i).append(' ').append(j).append(' ').append(t).println();
    }
}
