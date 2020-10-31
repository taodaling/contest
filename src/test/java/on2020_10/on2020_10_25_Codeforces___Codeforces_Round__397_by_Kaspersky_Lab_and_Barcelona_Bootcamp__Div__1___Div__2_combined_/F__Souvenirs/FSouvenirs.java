package on2020_10.on2020_10_25_Codeforces___Codeforces_Round__397_by_Kaspersky_Lab_and_Barcelona_Bootcamp__Div__1___Div__2_combined_.F__Souvenirs;



import template.io.FastInput;
import template.problem.IntervalNearestValuePair;

import java.io.PrintWriter;

public class FSouvenirs {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);
        int m = in.readInt();
        IntervalNearestValuePair.Query[] qs = new IntervalNearestValuePair.Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new IntervalNearestValuePair.Query(in.readInt() - 1, in.readInt() - 1);
        }
        IntervalNearestValuePair.solve(a, qs);
        for(IntervalNearestValuePair.Query q : qs){
            out.println(q.ans);
        }
    }
}
