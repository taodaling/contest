package on2020_05.on2020_05_10_.Nim;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MinSumXorZeroProblem;

public class Nim {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        long ans = MinSumXorZeroProblem.solve(a);
        out.println(ans);
    }
}
