package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Increasing_Subsequence;



import template.algo.LIS;
import template.io.FastInput;
import java.io.PrintWriter;
import java.util.Comparator;

public class IncreasingSubsequence {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] seq = new int[n];
        in.populate(seq);
        int ans = LIS.lisLength(i -> seq[i], n, Comparator.<Integer>naturalOrder());
        out.println(ans);
    }
}
