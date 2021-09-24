package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.E___Snack;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ESnack {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] A = in.rl(n);
        long[] Z = in.rl(m);
        long[] B = in.rl(m);
        Randomized.shuffle(A);
        Arrays.sort(A);
        List<Event> events = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            //B[i] + (k - n) Z[i] <= 0
            //n - k >= B[i] / Z[i]
            //k <= n - B[i] / Z[i]
            long k = n - DigitUtils.ceilDiv(B[i], Z[i]);
            events.add(new Event(i, k));
        }
        events.sort(Comparator.comparingLong(x -> x.time));
        int iter = 0;
        long sumB = Arrays.stream(B).sum();
        long sumZ = Arrays.stream(Z).sum();
        long totalZ = sumZ;
        long sumA = 0;
        long inf = (long) 2e18;
        long best = inf;
        for (int i = 0; i <= n; i++) {
            if (i > 0) {
                sumA += A[i - 1];
            }
            while (iter < events.size() && events.get(iter).time < i) {
                Event e = events.get(iter++);
                sumB -= B[e.index];
                sumZ -= Z[e.index];
            }
            best = Math.min(best, sumA + sumB - sumZ * (n - i) + totalZ * (n - i));
        }
        debug.debug("best", best);
        out.println(best);
    }

    Debug debug = new Debug(false);
}

class Event {
    int index;
    long time;

    public Event(int index, long time) {
        this.index = index;
        this.time = time;
    }
}