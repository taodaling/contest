package on2020_04.on2020_04_23_TopCoder_SRM__780.Prominence;



import template.primitve.generated.datastructure.IntegerRMQ;
import template.utils.Debug;

import java.util.Map;
import java.util.TreeMap;

public class Prominence {
    //Debug debug = new Debug(true);
    public long sumOfProminences(int N, int[] coef, int[] idx, int[] val) {
        int[] H = new int[N];

        for (int i = 0; i <= N - 1; i++) {
            int parity = i % 2;
            int a = coef[3 * parity];
            int b = coef[3 * parity + 1];
            int c = coef[3 * parity + 2];
            H[i] = (int) (((((long) a * i + b) % 1000000007) * i + c) % 1000000007);
        }
        for (int j = 0; j <= idx.length - 1; j++) {
            H[idx[j]] = val[j];
        }

        int[] left = new int[N];
        int[] right = new int[N];
        TreeMap<Integer, Integer> prev = new TreeMap<>();
        TreeMap<Integer, Integer> next = new TreeMap<>();
        for (int i = 0; i < N; i++) {
            Map.Entry<Integer, Integer> ceil = prev.ceilingEntry(H[i] + 1);
            if (ceil == null) {
                left[i] = 0;
            } else {
                left[i] = ceil.getValue() + 1;
            }
            while (!prev.isEmpty() && prev.firstKey() <= H[i]) {
                prev.pollFirstEntry();
            }
            prev.put(H[i], i);
        }
        for (int i = N - 1; i >= 0; i--) {
            Map.Entry<Integer, Integer> ceil = next.ceilingEntry(H[i] + 1);
            if (ceil == null) {
                right[i] = N - 1;
            } else {
                right[i] = ceil.getValue() - 1;
            }
            while (!next.isEmpty() && next.firstKey() <= H[i]) {
                next.pollFirstEntry();
            }
            next.put(H[i], i);
        }

//        debug.debug("H", H);
//        debug.debug("left", left);
//        debug.debug("right", right);
        long sum = 0;
        IntegerRMQ rmq = new IntegerRMQ(H, (a, b) -> Integer.compare(a, b));
        int[] contrib = new int[N];
        for (int i = 0; i < N; i++) {
            boolean top = true;
            if (i > 0 && H[i - 1] >= H[i]) {
                top = false;
            }
            if (i + 1 < N && H[i + 1] >= H[i]) {
                top = false;
            }
            if (!top) {
                continue;
            }

            int local = 0;
            if (left[i] == 0 && right[i] == N - 1) {
            } else {
                if(left[i] > 0){
                    local = Math.max(local, H[rmq.query(left[i], i)]);
                }
                if(right[i] < N - 1){
                    local = Math.max(local, H[rmq.query(i, right[i])]);
                }
            }
            contrib[i] = H[i] - local;
            sum += contrib[i];
        }
//debug.debug("contrib", contrib);
        return sum;
    }
}
