package contest;

import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongMinQueue;

import java.util.Arrays;

public class SteelMill {
    public long cheapest(int goal, int[] shipmentCost, int[] shipmentSize, int[] costPerKg) {
        long[] last = new long[goal + 1];
        long[] next = new long[goal + 1];
        long inf = (long) 1e18;
        Arrays.fill(last, inf);
        last[0] = 0;

        int n = shipmentCost.length;
        LongMinQueue dq = new LongMinQueue(goal + 1, LongComparator.NATURE_ORDER);
        for (int i = 0; i < n; i++) {
            dq.reset();
            dq.addLast(0);
            for (int j = 1; j <= goal; j++) {
                long extra = (long) j * costPerKg[i];
                next[j] = Math.min(last[j], dq.min() + extra + shipmentCost[i]);
                dq.addLast(last[j] - extra);
                if (dq.size() > shipmentSize[i]) {
                    dq.removeFirst();
                }
            }

            long[] tmp = last;
            last = next;
            next = tmp;
        }

        return last[goal];
    }
}
