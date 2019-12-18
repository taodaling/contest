package contest;


import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.problem.OnCircleMinCostMatchProblem;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

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
