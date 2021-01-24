package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class HEOI2015Z {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i], 0);
        }
        int idIndicator = 0;
        int[][] id = new int[n][m];
        IntegerArrayList uList = new IntegerArrayList(n * m * 2);
        IntegerArrayList vList = new IntegerArrayList(n * m * 2);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '*') {
                    continue;
                }
                id[i][j] = idIndicator++;
                if (i > 0 && mat[i - 1][j] == '.') {
                    uList.add(id[i - 1][j]);
                    vList.add(id[i][j]);
                }
                if (j > 0 && mat[i][j - 1] == '.') {
                    uList.add(id[i][j - 1]);
                    vList.add(id[i][j]);
                }
            }
        }

        int ans = MatrixTreeTheorem.countUndirectedGraphMSTModPrimeModPositive(idIndicator, uList.toArray(), vList.toArray(), (int)1e9);
        out.println(ans);
    }
}
