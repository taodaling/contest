package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.PlantTreeProblem;

public class BZOJ1150 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = in.readInt();
        }
        long[] ws = new long[n];
        for (int i = 0; i < n - 1; i++) {
            ws[i] = xs[i] - xs[i + 1];
        }
        ws[n - 1] = (long) -1e18;
        PlantTreeProblem problem = new PlantTreeProblem(ws, k);
        out.println(-problem.getAnswer());
    }
}
