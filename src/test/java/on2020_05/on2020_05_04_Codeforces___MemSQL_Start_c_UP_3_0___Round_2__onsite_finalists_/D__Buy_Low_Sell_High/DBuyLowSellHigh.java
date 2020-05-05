package on2020_05.on2020_05_04_Codeforces___MemSQL_Start_c_UP_3_0___Round_2__onsite_finalists_.D__Buy_Low_Sell_High;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.PriorityQueue;

public class DBuyLowSellHigh {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long ans = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(n);
        for (int i = 0; i < n; i++) {
            int p = in.readInt();
            if (!pq.isEmpty() && pq.peek() < p) {
                int val = pq.poll();
                ans += p - val;
                pq.add(p);
            }
            pq.add(p);
            debug.debug("pq", pq);
            debug.debug("ans", ans);
        }

        out.println(ans);
    }
}
