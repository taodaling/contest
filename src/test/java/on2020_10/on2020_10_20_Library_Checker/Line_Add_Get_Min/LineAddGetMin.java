package on2020_10.on2020_10_20_Library_Checker.Line_Add_Get_Min;



import template.geometry.LongConvexHullTrick;
import template.io.FastInput;

import java.io.PrintWriter;

public class LineAddGetMin {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();
        LongConvexHullTrick cht = new LongConvexHullTrick();
        for (int i = 0; i < n; i++) {
            cht.insert(-in.readInt(), -in.readInt());
        }
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                cht.insert(-in.readInt(), -in.readInt());
            } else {
                long ans = -cht.query(in.readLong());
                out.println(ans);
            }
        }
    }
}
