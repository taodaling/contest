package contest;

import template.io.FastInput;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class P4718PollardRho {
    Map<Long, Long> cache = new HashMap<>();

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        long n = in.readLong();
        if (!cache.containsKey(n)) {
            cache.put(n, LongPollardRho.findAllFactors(n).stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get());
        }
        long ans = cache.get(n);
        out.println(ans == n ? "Prime" : ans);
    }
}
