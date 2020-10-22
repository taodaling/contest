package on2020_10.on2020_10_21_Library_Checker.Task;





import template.algo.MatroidIndependentSet;
import template.algo.MatroidIntersect;
import template.algo.MaximumWeightMatroidIntersect;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class Task {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] color1 = new int[n];
        int[] color2 = new int[n];
        in.populate(color1);
        in.populate(color2);
        long[] w = new long[n];
        in.populate(w);
        MatroidIntersect mi = new MaximumWeightMatroidIntersect(n, w);
        boolean[] ans = mi.intersect(MatroidIndependentSet.ofColor(color1),
                MatroidIndependentSet.ofColor(color2));
        int size = 0;
        for (int i = 0; i < n; i++) {
            size += ans[i] ? 1 : 0;
        }
        out.println(size);
        for (int i = 0; i < n; i++) {
            if (ans[i]) {
                out.println(i);
            }
        }
    }
}
