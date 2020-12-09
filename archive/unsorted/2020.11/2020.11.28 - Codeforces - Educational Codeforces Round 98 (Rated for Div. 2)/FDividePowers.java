package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class FDividePowers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] cnts = new long[n];
        in.populate(cnts);

        for (int i = 0; i < n; i++) {
            int t = in.ri();
            if (t == 1) {
                cnts[in.ri()] = in.ri();
            } else {

            }
        }
    }

    //require cnts[0] + ... + cnts[t] >= k
    public long solve(int[] cnts, int t, long k) {
        long ans = 0;
        long cur = 0;
        for(int i = 0; i <= t; i++){
            if(cnts[t]
        }
    }
}
