package contest;



import template.geometry.LongConvexHullTrick;
import template.io.FastInput;

import java.io.PrintWriter;

public class LineAddGetMin {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();
        LongConvexHullTrick cht = new LongConvexHullTrick();
        for (int i = 0; i < n; i++) {
            cht.insert(-in.readLong(), -in.readLong());
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                cht.insert(-in.readLong(), -in.readLong());
            } else {
                long ans = -cht.query(in.readLong());
                out.println(ans);
            }
        }
    }
}
