package on2021_04.on2021_04_11_Codeforces___Codeforces_Round__213__Div__1_.E__Empty_Rectangles;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.problem.RectOnGridProblem;

import java.util.Arrays;

public class EEmptyRectangles {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc() - '0';
            }
        }

        long ans = RectOnGridProblem.countExactlyKOneRect(mat, k);
        out.println(ans);

    }
}
