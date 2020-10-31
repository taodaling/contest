package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Increasing_Array;



import template.io.FastInput;

import java.io.PrintWriter;

public class IncreasingArray {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int max = 0;
        long ans = 0;
        for (int x : a) {
            ans += Math.max(0, max - x);
            max = Math.max(max, x);
        }
        out.println(ans);
    }
}
