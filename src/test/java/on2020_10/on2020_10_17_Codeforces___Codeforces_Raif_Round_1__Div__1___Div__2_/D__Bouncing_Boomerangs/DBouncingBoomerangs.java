package on2020_10.on2020_10_17_Codeforces___Codeforces_Raif_Round_1__Div__1___Div__2_.D__Bouncing_Boomerangs;



import template.graph.DinicBipartiteMatch;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DBouncingBoomerangs {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] right = new int[n];
        Deque<Integer> one = new ArrayDeque<>(n);
        Deque<Integer> two = new ArrayDeque<>(n);
        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == 1) {
                one.addLast(i);
                continue;
            }
            if (a[i] == 0) {
                continue;
            }
            if (a[i] == 3 && !two.isEmpty()) {
                right[i] = two.removeFirst();
                two.addLast(i);
                continue;
            }
            if (one.isEmpty()) {
                out.println(-1);
                return;
            }
            right[i] = one.removeFirst();
            two.addLast(i);
        }
        int height = n - 1;
        int[] y = new int[n];
        List<int[]> ans = new ArrayList<>(n * 2);
        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == 1) {
                y[i] = height--;
                ans.add(new int[]{i, y[i]});
                continue;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (a[i] == 2) {
                y[i] = y[right[i]];
                ans.add(new int[]{i, y[i]});
            }
            if (a[i] == 3) {
                y[i] = height--;
                ans.add(new int[]{i, y[i]});
                ans.add(new int[]{right[i], y[i]});
            }
        }

        out.println(ans.size());
        for (int[] pair : ans) {
            out.print(pair[1] + 1);
            out.append(' ');
            out.println(pair[0] + 1);
        }
    }
}
