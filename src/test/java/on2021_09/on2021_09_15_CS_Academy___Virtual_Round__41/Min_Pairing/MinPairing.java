package on2021_09.on2021_09_15_CS_Academy___Virtual_Round__41.Min_Pairing;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class MinPairing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long ans = 0;
        for(int i = 0; i < n; i += 2){
            ans += a[i + 1] - a[i];
        }
        out.println(ans);
    }
}
