package on2020_06.on2020_06_15_Codeforces___300iq_Contest_3.F__Farm_of_Monsters;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Comparator;
import java.util.PriorityQueue;

public class FFarmOfMonsters {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int[] h = new int[n];
        in.populate(h);

        long pt = 1;
        PriorityQueue<Integer> pq = new PriorityQueue<>(n, (x, y) -> y.compareTo(x));
        for (int health : h) {
            debug.debug("pq", pq);
            debug.debug("pt", pt);
            int time = DigitUtils.ceilDiv(health, b);
            pt += time - 1;
            int cost = DigitUtils.ceilDiv(health - (time - 1) * b, a);
            if (pt >= cost) {
                pt -= cost;
                pq.add(cost);
                continue;
            }
            if (!pq.isEmpty() && pq.peek() > cost) {
                pt += pq.remove() + 1;
                pt -= cost;
                pq.add(cost);
                continue;
            }
            pt++;
        }

        debug.debug("pq", pq);
        debug.debug("pt", pt);


        out.println(pq.size());
    }
}
