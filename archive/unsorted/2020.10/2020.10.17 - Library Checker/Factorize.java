package contest;

import template.io.FastInput;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.LongArrayList;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Factorize {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
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
        out.print(ans.size());
        out.append(' ');
        for (int i = 0; i < ans.size(); i++) {
            out.print(ans.get(i));
            out.append(' ');
        }
        out.println();
    }
}
