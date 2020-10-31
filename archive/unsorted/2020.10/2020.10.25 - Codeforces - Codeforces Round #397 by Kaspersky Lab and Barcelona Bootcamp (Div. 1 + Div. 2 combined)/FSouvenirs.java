package contest;

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
