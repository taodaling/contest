package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Apartments;



import template.io.FastInput;
import template.rand.Randomized;

import java.io.PrintWriter;
import java.util.Arrays;

public class Apartments {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];
        in.populate(a);
        in.populate(b);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        int l = 0;
        int ans = 0;
        for (int x : a) {
            while (l < m && b[l] + k < x) {
                l++;
            }
            if (l < m && b[l] + k >= x && b[l] - k <= x) {
                l++;
                ans++;
            }
        }
        out.println(ans);
    }
}
