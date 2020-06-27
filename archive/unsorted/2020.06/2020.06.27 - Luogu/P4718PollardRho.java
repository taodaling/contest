package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongPollardRho;

import java.util.*;
import java.util.stream.Collectors;

public class P4718PollardRho {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = LongPollardRho.findAllFactors(n).stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get();
        out.println(ans == n ? "Prime" : ans);
    }
}
