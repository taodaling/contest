package on2021_09.on2021_09_23_AtCoder___AtCoder_Beginner_Contest_218.H___Red_and_Blue_Lamps;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.PlantTreeProblem;

import java.util.Arrays;

public class HRedAndBlueLamps {

    static double inf = 1e50;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int r = in.ri();
        int[] a = in.ri(n - 1);
        r = Math.min(r, n - r);
        int even = n / 2;
        if (r >= even) {
            long ans = 0;
            for (int x : a) {
                ans += x;
            }
            out.println(ans);
            return;
        }
        a = Arrays.copyOf(a, n);
        long[] weights = new long[n];
        for (int i = 0; i < n; i++) {
            weights[i] = a[i] + a[(i + 1) % n];
        }
        PlantTreeProblem pt = new PlantTreeProblem(weights, r);
        long ans = pt.getAnswer();
        out.println(ans);
    }
}
