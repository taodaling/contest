package contest;

import template.problem.CandyAssignProblem;
import template.io.FastInput;
import template.io.FastOutput;

public class BZOJ3293 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int sum = 0;
        CandyAssignProblem problem = new CandyAssignProblem(n, n);
        int[] coins = new int[n];
        for(int i = 0; i < n; i++){
            coins[i] = in.readInt();
            sum += coins[i];
        }

        int avg = sum / n;
        for(int i = 0; i < n; i++){
            problem.requestOn(i, coins[i], avg);
        }

        problem.solve();
        out.println(problem.minimumCost());
    }
}
