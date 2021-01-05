package on2020_12.on2020_12_25_Luogu.P4718_____Pollard_Rho__;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class P4718PollardRho {
    Map<Long, Long> cache = new HashMap<>();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        if (!cache.containsKey(n)) {
            cache.put(n, LongPollardRho.findAllFactors(n).stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get());
        }
        long ans = cache.get(n);
        out.println(ans == n ? "Prime" : ans);
    }
}
