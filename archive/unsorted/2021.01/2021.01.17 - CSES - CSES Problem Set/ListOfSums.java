package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RecoveryFromPairSum;

public class ListOfSums {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] sums = in.rl(n * (n - 1) / 2);
        long[] ans = RecoveryFromPairSum.recover(n, sums);
        for(long x : ans){
            out.println(x);
        }
    }
}
