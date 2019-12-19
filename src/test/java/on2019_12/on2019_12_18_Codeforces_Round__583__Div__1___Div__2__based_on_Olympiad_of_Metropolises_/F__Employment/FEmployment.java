package on2019_12.on2019_12_18_Codeforces_Round__583__Div__1___Div__2__based_on_Olympiad_of_Metropolises_.F__Employment;




import template.io.FastInput;
import template.io.FastOutput;
import template.problem.OnCircleMinCostMatchProblem;

public class FEmployment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() - 1;
        }
        for (int j = 0; j < n; j++) {
            b[j] = in.readInt() - 1;
        }

        OnCircleMinCostMatchProblem problem = new OnCircleMinCostMatchProblem(m, a, b);
        out.println(problem.getMinimumCost());

        for (int i = 0; i < n; i++) {
            out.append(problem.getMateOf(i) + 1).append(' ');
        }
    }

}
