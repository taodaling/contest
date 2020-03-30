package contest;



import template.graph.KMAlgo;
import template.io.FastInput;
import template.io.FastOutput;

public class P1559 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        int[][] boyToGirl = new int[n][n];
        int[][] girlToBoy = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boyToGirl[i][j] = in.readInt();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                girlToBoy[i][j] = in.readInt();
            }
        }

        long[][] mat = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = (long)boyToGirl[i][j] * girlToBoy[j][i];
            }
        }
        KMAlgo km = new KMAlgo(mat);
        long ans = km.solve();
        out.println(ans);
    }
}
