package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.A__Searching_for_Graph;



import template.io.FastInput;
import template.io.FastOutput;

public class ASearchingForGraph {
    boolean[][] adj;

    public void add(int a, int b) {
        adj[a][b] = adj[b][a] = true;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        adj = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            add(i, (i + 1) % n);
            add(i, (i + 2) % n);
        }
        int remain = p;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j]) {
                    continue;
                }
                if (remain > 0) {
                    add(i, j);
                    remain--;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j]) {
                    out.append(i + 1).append(' ').append(j + 1).println();
                }
            }
        }
        out.println();
    }
}
