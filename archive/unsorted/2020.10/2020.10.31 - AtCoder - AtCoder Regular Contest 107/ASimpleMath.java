package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASimpleMath {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        long ans = sum(a) * sum(b) % mod * sum(c) % mod;
        out.println(ans);
    }
    int mod = 998244353;

    //1 + 2 + ... + n
    public long sum(long n){
        return n * (n + 1) / 2 % mod;
    }
}
