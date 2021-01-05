package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.rand.Randomized;

import java.util.Arrays;

public class ARowGCD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] a = new long[n];
        long[] b = new long[m];
        in.populate(a);
        in.populate(b);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long g = 0;
        for(int i = 1; i < n; i++){
            g = GCDs.gcd(g, a[i] - a[0]);
        }
        for(int i = 0; i < m; i++){
            long ans = GCDs.gcd(g, a[0] + b[i]);
            out.println(ans);
        }
    }
}
