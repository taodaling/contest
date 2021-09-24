package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Sum_of_Squares0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class SumOfSquares {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] Wprev = new int[n + 1];
        Wprev[0] = 1;
        int[] Sprev = new int[n + 1];
        int[] snapshot = null;
        for (int i = 1; i <= k; i++) {
            if (i == k) {
                snapshot = Sprev.clone();
            }
            for (int j = 0; j <= n; j++) {
                if (i + j <= n) {
                    int ij = i + j;
                    Sprev[ij] += (Sprev[j] + (i + 2L * j) * Wprev[j]) % mod;
                    Wprev[ij] += Wprev[j];
                    if(Sprev[ij] >= mod){
                        Sprev[ij] -= mod;
                    }
                    if(Wprev[ij] >= mod){
                        Wprev[ij] -= mod;
                    }
                }
            }

            debug.debug("i", i);
            debug.debug("Wprev", Wprev);
            debug.debug("Sprev", Sprev);
        }

        long ans = Sprev[n] - snapshot[n];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
