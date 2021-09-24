package on2021_07.on2021_07_26_Codeforces___Codeforces_Round__150__Div__1_.E__Matrix;



import template.datastructure.PQTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;

public class EMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] mat = new char[n][n];
        PQTree pq = new PQTree(n);
        boolean[] set = new boolean[n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(set, false);
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.rc();
                if (mat[i][j] == '1') {
                    set[j] = true;
                }
            }
            pq.update(set);
        }
        if (!pq.possible()) {
            out.println("NO");
            return;
        }
        out.println("YES");
        List<Integer> sol = pq.getOrder();
        char[][] ans = new char[n][n];
        for (int i = 0; i < n; i++) {
            int col = sol.get(i);
            for (int j = 0; j < n; j++) {
                ans[j][i] = mat[j][col];
            }
        }
        for(int i = 0; i < n; i++){
            out.println(ans[i]);
        }
    }
}
