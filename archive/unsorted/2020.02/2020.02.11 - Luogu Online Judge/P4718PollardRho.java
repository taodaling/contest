package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongMillerRabin;
import template.math.LongPollardRho;

import java.util.Comparator;
import java.util.stream.Collectors;

public class P4718PollardRho {
    LongPollardRho pollardRho = new LongPollardRho();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        //System.err.println(n);
        long ans = pollardRho.findAllFactors(n)
                .keySet().stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get();
        out.println(ans == n ? "Prime" : ans);
    }
}
