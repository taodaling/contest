package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.CandyAssignProblem;

public class FoodDivision {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);
        CandyAssignProblem cap = new CandyAssignProblem(n, n);
        for (int i = 0; i < n; i++) {
            cap.assignCandyOn(i, a[i]);
            cap.requireCandyOn(i, b[i]);
        }
        long ans = cap.solve();
        out.println(ans);
    }
}
