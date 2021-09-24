package on2021_09.on2021_09_16_CS_Academy___Round__41.Add_and_Subtract;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.InterleavedMaximumSumSubsequence;

public class AddAndSubtract {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        long[] ans = new long[n + 1];
        InterleavedMaximumSumSubsequence.solve(a,
                new InterleavedMaximumSumSubsequence.Callback() {
                    @Override
                    public void callback(int size, long sum, boolean[] set) {
                        ans[size] = sum;
                    }
                });
        for(int i = 1; i <= n; i++){
            out.println(ans[i]);
        }
    }


}



