package on2020_06.on2020_06_15_Codeforces___300iq_Contest_3.I__Ignore_Submasks;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class IIgnoreSubmasks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        long[] a = new long[n];
        in.populate(a);

        int ans = 0;
        long union = 0;
        Modular mod = new Modular(998244353);
        for (int i = 0; i < n; i++) {
            long x = a[i];
            long next = union | x;
            long cnt = (1L << k - Long.bitCount(union)) - (1L << k - Long.bitCount(next));
            ans = mod.plus(ans, mod.mul(mod.valueOf(cnt), i + 1));
            union |= x;
        }

        out.println(ans);
    }
}
