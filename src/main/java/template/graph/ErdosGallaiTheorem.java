package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.primitve.generated.utils.IntegerBinaryConsumer;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

/**
 * Check whether there is a way to construct a simple undirected graph with specified degrees.
 */
public class ErdosGallaiTheorem {
    /**
     * Build a graph in O(\sum_{i} degs[i])
     *
     * @param degs
     * @param consumer
     */
    public static void buildGraph(int[] degs, IntegerBinaryConsumer consumer) {
        IntegerPriorityQueue pq = new IntegerPriorityQueue(degs.length, (i, j) ->
                -Integer.compare(degs[i], degs[j]), i -> i);
        IntegerArrayList buf = new IntegerArrayList(degs.length);
        while (!pq.isEmpty()) {
            int head = pq.pop();
            while (degs[head] > 0) {
                int x = pq.pop();
                degs[head]--;
                degs[x]--;
                buf.add(x);
                consumer.accept(head, x);
            }
            while (!buf.isEmpty()) {
                pq.add(buf.pop());
            }
        }
    }

    public static boolean possible(int[] degs) {
        return maxOnAllK(degs) <= 0;
    }

    /**
     * <pre>
     *  O(n) solution
     * </pre>
     * <pre>
     * return Long.MAX_VALUE for impossible
     * </pre>
     */
    public static long maxOnAllK(int[] degs) {
        long sum = 0;
        int min = 0;
        for (int i = 0; i < degs.length; i++) {
            sum += degs[i];
            min = Math.min(min, degs[i]);
        }
        if (sum % 2 == 1 || min < 0) {
            return Long.MAX_VALUE;
        }
        CompareUtils.radixSort(degs, 0, degs.length - 1);
        SequenceUtils.reverse(degs, 0, degs.length - 1);
        int n = degs.length;
        IntegerPreSum ps = new IntegerPreSum(i -> degs[i], degs.length);
        long[] ss = new long[n + 1];
        int rIter = n;
        for (int i = n - 1; i >= 0; i--) {
            int l = i;
            int r = n - 1;
            if (rIter - 1 >= l && degs[rIter - 1] < i) {
                rIter--;
            }
            while (rIter < n && degs[rIter] > i) {
                rIter++;
            }
            ss[i] = (long) (rIter - l) * i + ps.intervalSum(rIter, r);
        }

        long max = Long.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, ps.intervalSum(0, i - 1) - (long) i * (i - 1) - ss[i]);
        }
        return max;
    }
}
