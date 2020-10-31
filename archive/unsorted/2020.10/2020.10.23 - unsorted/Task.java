package contest;

import template.io.FastInput;
import template.problem.ExpKPrefixSum;

import java.io.PrintWriter;

public class Task {
    int mod = (int) (1e9 + 7);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int ans = new ExpKPrefixSum(10000, mod).solve(k, n, mod);
        out.println(ans);
    }
}
