package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class ARoadToCinema {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int s = in.ri();
        int t = in.ri();
        int[][] car = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                car[i][j] = in.ri();
            }
        }
        int[] gas = new int[k + 1];
        for (int i = 0; i < k; i++) {
            gas[i] = in.ri();
        }
        gas[k] = s;
        Randomized.shuffle(gas);
        Arrays.sort(gas);
        IntPredicate predicate = m -> {
            int bestCap = -1;
            for (int i = 0; i < n; i++) {
                if (car[i][0] <= m) {
                    bestCap = Math.max(bestCap, car[i][1]);
                }
            }
            if (bestCap < 0) {
                return false;
            }
            int time = 0;
            int now = 0;
            for (int i = 0; i <= k; i++) {
                int to = gas[i];
                int len = to - now;
                if (len > bestCap) {
                    return false;
                }
                time += 2 * len - Math.min(len, bestCap - len);
                now = to;
            }
            return time <= t;
        };
        int ans = BinarySearch.firstTrue(predicate, 0, (int)1e9);
        if(ans > 1e9){
            out.println(-1);
            return;
        }
        out.println(ans);
    }
}
