package on2021_05.on2021_05_24_Codeforces___Codeforces_Round__722__Div__1_.B__Kavi_on_Pairing_Duty;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.Debug;

public class BKaviOnPairingDuty {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] way = new long[n + 1];
        long[] ps = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j += i) {
                way[j] += 1;
            }
        }
        for (int i = 1; i <= n; i++) {
            way[i] += ps[i - 1];
            way[i] %= mod;
            ps[i] = ps[i - 1] + way[i];
            ps[i] %= mod;
        }
        debug.debug("way", way);
        long ans = way[n];
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
