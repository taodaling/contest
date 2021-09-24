package on2021_09.on2021_09_19_AtCoder___AtCoder_Regular_Contest_126.C___Maximize_GCD;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Arrays;

public class CMaximizeGCD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long k = in.rl();
        int[] a = in.ri(n);
        long mx = Arrays.stream(a).max().orElse(-1);
        long sum = 0;
        for (int x : a) {
            sum += x;
        }
        if (mx * n - sum <= k) {
            k -= mx * n - sum;
            long ans = mx + k / n;
            out.println(ans);
            return;
        }
        int L = (int) mx;
        int[] cnt = new int[L + 1];
        for (int x : a) {
            cnt[x]++;
        }
        LongPreSum weightPS = new LongPreSum(i -> cnt[i] * (long) i, L + 1);
        LongPreSum onePS = new LongPreSum(i -> cnt[i], L + 1);
        long best = 1;
        for (int g = 1; g <= L; g++) {
            long cost = 0;
            for (int j = 1; j <= L; j += g) {
                int l = j;
                int r = j + g - 1;
                cost += onePS.intervalSum(l, r) * r - weightPS.intervalSum(l, r);
            }
            if(cost <= k){
                best = Math.max(best, g);
            }
        }
        out.println(best);
    }
}
