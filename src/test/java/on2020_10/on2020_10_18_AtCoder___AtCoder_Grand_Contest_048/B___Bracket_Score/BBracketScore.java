package on2020_10.on2020_10_18_AtCoder___AtCoder_Grand_Contest_048.B___Bracket_Score;



import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BBracketScore {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long[] a = new long[n];
        long[] b = new long[n];
        in.populate(a);
        in.populate(b);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] -= b[i];
            sum += b[i];
        }
        PriorityQueue<Long> x = new PriorityQueue<>(n, Comparator.reverseOrder());
        PriorityQueue<Long> y = new PriorityQueue<>(n, Comparator.reverseOrder());
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                x.add(a[i]);
            } else {
                y.add(a[i]);
            }
        }
        while(!x.isEmpty()){
            long xh = x.remove();
            long yh = y.remove();
            sum += Math.max(0, xh + yh);
        }
        out.println(sum);
    }
}
