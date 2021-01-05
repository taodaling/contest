package on2020_12.on2020_12_25_Library_Checker.Factorize;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Factorize {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        Set<Long> bigPrimes = LongPollardRho.findAllFactors(x);
        List<Long> ans = new ArrayList<>(60);
        for (long factor : bigPrimes) {
            while (x % factor == 0) {
                x /= factor;
                ans.add(factor);
            }
        }

        ans.sort(Comparator.naturalOrder());
        out.append(ans.size());
        out.append(' ');
        for (int i = 0; i < ans.size(); i++) {
            out.append(ans.get(i));
            out.append(' ');
        }
        out.println();
    }
}
