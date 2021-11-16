package on2021_11.on2021_11_16_Library_Checker.Associative_Array;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[][] qs = new long[n][];
        List<Long> K = new ArrayList<>(n);
        List<Long> V = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int t = in.ri();
            long k = in.rl();
            if (t == 0) {
                long v = in.rl();
                qs[i] = new long[]{t, k, v};
            }else{
                qs[i] = new long[]{t, k};
            }
        }
    }
}
