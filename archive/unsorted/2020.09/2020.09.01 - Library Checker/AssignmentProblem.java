package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongKMAlgo;

public class AssignmentProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[][] mat = new long[n][n];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = -in.readInt();
            }
        }

        LongKMAlgo km = new LongKMAlgo(mat);
        long ans = -km.solve();
        out.println(ans);
        for(int i = 0; i < n; i++){
            out.append(km.getLeftPartner(i)).append(' ');
        }
    }
}
