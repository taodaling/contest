package on2021_06.on2021_06_03_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.F__Favorite_Game;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class FFavoriteGame {
    int[][] distToTower;
    int[][] distToTask;
    int[][] tower;
    int[][] task;
    int inf = (int) 1e9 + 10;
    int[][] f;
    int[][] g;
    int n;
    int m;

    public int dist(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        tower = new int[n][2];
        task = new int[m][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                tower[i][j] = in.ri();
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                task[i][j] = in.ri();
            }
        }

        distToTask = new int[1 << n][m];
        distToTower = new int[1 << n][n];
        for (int i = 0; i < 1 << n; i++) {
            for (int j = 0; j < m; j++) {
                distToTask[i][j] = inf;
                for (int k = 0; k < n; k++) {
                    if (Bits.get(i, k) == 1) {
                        distToTask[i][j] = Math.min(distToTask[i][j], dist(tower[k], task[j]));
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                distToTower[i][j] = inf;
                for (int k = 0; k < n; k++) {
                    if (Bits.get(i, k) == 1) {
                        distToTower[i][j] = Math.min(distToTower[i][j], dist(tower[k], tower[j]));
                    }
                }
            }
        }

        f = new int[m + 1][1 << n];
        g = new int[m][1 << n];
        SequenceUtils.deepFill(f, inf);
        for(int i = 0; i < n; i++){
            f[0][1 << i] = 0;
        }

    }
}
