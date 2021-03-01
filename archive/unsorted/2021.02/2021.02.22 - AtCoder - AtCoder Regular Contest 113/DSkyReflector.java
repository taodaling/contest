package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class DSkyReflector {
    int mod = 998244353;
    Power pow = new Power(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        if(n == 1){
            out.println(pow.pow(k, m));
            return;
        }
        if(m == 1){
            out.println(pow.pow(k, n));
            return;
        }
        long sum = 0;
        for(int i = 1; i <= k; i++){
            sum += (long)(pow.pow(i, n) - pow.pow(i - 1, n)) * pow.pow(k - i + 1, m) % mod;
        }
        sum %= mod;
        if(sum < 0){
            sum += mod;
        }
        out.println(sum);
    }
}
