package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerKM;

public class TaskAssignment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] costs = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                costs[i][j] = -in.ri();
            }
        }
        IntegerKM km = new IntegerKM(costs);
        out.println(-km.solve());
        for(int i = 0; i < n; i++){
            out.append(i + 1).append(' ').append(km.getLeftPartner(i) + 1).println();
        }
    }
}
