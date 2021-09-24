package on2021_08.on2021_08_30_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2021_Qualification_Round.B__Xs_and_Os;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class BXsAndOs {
    int[] type = new int[128];

    {
        type['.'] = 0;
        type['X'] = 1;
        type['O'] = 2;
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);

        int n = in.ri();

        int[][] row = new int[3][n];
        int[][] col = new int[3][n];
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int t = type[in.rc()];
                row[t][i]++;
                col[t][j]++;
                mat[i][j] = t;
            }
        }

        debug.debug("row", row);
        debug.debug("col", col);
        int inf = (int) 1e9;
        int minCost = inf;
        int way = 0;
        for (int i = 0; i < n; i++) {
            if (row[2][i] > 0) {
                continue;
            }
            int cand = row[0][i];
            if (minCost > cand) {
                minCost = cand;
                way = 0;
            }
            if (minCost == cand) {
                way++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (col[2][i] > 0) {
                continue;
            }
            int cand = col[0][i];
            if (minCost > cand) {
                minCost = cand;
                way = 0;
            }
            if (minCost == cand) {
                way++;
            }
        }
        if (minCost == 1) {
            way = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(mat[i][j] != 0){
                        continue;
                    }
                    if (row[2][i] == 0 && row[0][i] == 1 ||
                            col[2][j] == 0 && col[0][j] == 1) {
                        way++;
                    }
                }
            }
        }
        if (minCost == inf) {
            out.println("Impossible");
        } else {
            out.append(minCost).append(' ').append(way).println();
        }
    }
}
