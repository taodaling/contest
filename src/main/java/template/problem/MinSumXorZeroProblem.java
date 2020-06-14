package template.problem;

import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerPriorityQueue;

/**
 * Given n non-negative number: a_1, a_2, ... , a_n. Find minimum sum of \sum_i x_i while x_i >= 0 and (a_1 + x_1) ^ ... ^ (a_n + x_n) = 0.
 * <br>
 * a_i < 2^{61}
 */
public class MinSumXorZeroProblem {
    public static long solve(long[] data) {
        long ans = solve(data, 0);
        for (int i = 0; i <= 61; i++) {
            long ret = solve(data, 1L << i);
            if (ans == -1 || ret != -1 && ret < ans) {
                ans = ret;
            }
        }
        return ans;
    }

    /**
     * O(60^2n)
     * @param data
     * @param mask
     * @return
     */
    private static long solve(long[] data, long mask) {
        long sum = 0;
        long[] arr = data.clone();
        IntegerPriorityQueue pq = new IntegerPriorityQueue(3, (a, b) -> Long.compare(arr[a], arr[b]));
        for (int i = 61; i >= 0; i--) {
            pq.clear();
            long tail = (1L << i) - 1 + (1L << i);
            for (int j = 0; j < arr.length; j++) {
                arr[j] &= tail;
            }
            int bit = 0;
            for (long a : arr) {
                bit ^= Bits.get(a, i);
            }
            for (int j = 0; j < arr.length; j++) {
                if (Bits.get(arr[j], i) != 0) {
                    continue;
                }
                pq.add(j);
                if (pq.size() > 2) {
                    pq.pop();
                }
            }
            if (Bits.get(mask, i) == 0) {
                if (bit == 0) {
                    continue;
                }
                if (pq.isEmpty()) {
                    return -1;
                }
                while (pq.size() > 1) {
                    pq.pop();
                }
                int which = pq.pop();
                sum += (1L << i) - arr[which];
                arr[which] = 1L << i;
            } else {
                //even
                if (bit != 0) {
                    return -1;
                }
                if (pq.size() < 2) {
                    return -1;
                }
                int a = pq.pop();
                int b = pq.pop();
                sum += (1L << i) - arr[a];
                sum += (1L << i) - arr[b];
                arr[b] = arr[a] = 1L << i;
            }
        }

        return sum;
    }
}
