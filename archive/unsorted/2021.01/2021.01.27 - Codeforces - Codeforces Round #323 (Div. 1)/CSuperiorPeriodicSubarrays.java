package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.rand.HashData;
import template.rand.IntRangeHash;
import template.string.KMPAutomaton;
import template.utils.Debug;

import java.util.Arrays;

public class CSuperiorPeriodicSubarrays {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] data = new int[n * 2];
        for (int i = 0; i < n; i++) {
            data[i] = data[i + n] = in.ri();
        }
        int[] coprime = new int[n + 1];
        int[] step = new int[n * 2];
        int[] max = new int[n * 2];
        boolean[] ok = new boolean[n * 2];
        long ans = 0;
        for (int g : Factorization.factorizeNumber(n).toArray()) {
            if(g >= n){
                continue;
            }
            int ng = n / g;
            coprime[0] = 0;
            for (int i = 1; i < ng; i++) {
                coprime[i] = GCDs.gcd(i, ng) == 1 ? 1 : 0;
                coprime[i] += coprime[i - 1];
            }
            Arrays.fill(max, 0);
            for (int i = 0; i < n; i++) {
                max[i % g] = Math.max(max[i % g], data[i]);
            }
            for (int i = 0; i < g; i++) {
                max[i + g] = max[i];
            }
            int diff = 0;
            for (int i = 0; i < g - 1; i++) {
                if (data[i] != max[i]) {
                    diff++;
                }
            }
            Arrays.fill(ok, false);
            for (int i = g - 1; i < 2 * n; i++) {
                if (data[i] != max[i % g]) {
                    diff++;
                }
                if (diff == 0) {
                    ok[i - g + 1] = true;
                }
                if (data[i - g + 1] != max[(i - g + 1) % g]) {
                    diff--;
                }
            }
            for (int i = 2 * n - 1; i >= 0; i--) {
                step[i] = ok[i] ? 1 : 0;
                if (ok[i] && i + g < 2 * n) {
                    step[i] += step[i + g];
                }
            }
            for (int i = 0; i < n; i++) {
                //great
                int l = Math.min(ng - 1, step[i]);
                ans += coprime[l];
            }
            debug.debug("g", g);
            debug.debug("ans", ans);
        }

        out.println(ans);
    }
}
