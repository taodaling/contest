package on2020_02.on2020_02_26_Codeforces_Round__492__Div__1___Thanks__uDebug__.A__Tesla;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ATesla {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        mat = new int[5][1 + n];
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        while (k > 0) {
            tryMatch();
            if (!tryLoop()) {
                if(k > 0){
                    out.println(-1);
                    return;
                }
            }
        }

        out.println(ans.size());
        for(int[] x : ans){
            out.append(x[0]).append(' ')
                    .append(x[1]).append(' ')
                    .append(x[2]).println();
        }
    }

    int n;
    int[][] mat;
    int k;
    List<int[]> ans = new ArrayList<>();

    public int[] next(int[] xy) {
        int[] ans = xy.clone();
        if (ans[0] == 3) {
            ans[1]--;
            if (ans[1] <= 0) {
                ans[1]++;
                ans[0]--;
            }
        } else {
            ans[1]++;
            if (ans[1] > n) {
                ans[1]--;
                ans[0]++;
            }
        }
        return ans;
    }

    public void move(int x1, int y1, int x2, int y2) {
        ans.add(new int[]{mat[x1][y1], x2, y2});
        mat[x2][y2] = mat[x1][y1];
        mat[x1][y1] = 0;
    }

    public boolean tryLoop() {
        int x = -1;
        int y = -1;
        for (int i = 2; i <= 3; i++) {
            for (int j = 1; j <= n; j++) {
                if (mat[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if (x == -1) {
            return false;
        }
        int[] now = new int[]{x, y};
        while (true) {
            int[] next = next(now);
            if (next[0] == x && next[1] == y) {
                break;
            }
            if (mat[next[0]][next[1]] != 0) {
                move(next[0], next[1], now[0], now[1]);
            }
            now = next;
        }

        return true;
    }

    public void tryMatch() {
        for (int i = 1; i <= n; i++) {
            if (mat[1][i] == mat[2][i] && mat[2][i] != 0) {
                move(2, i, 1, i);
                k--;
            }
            if (mat[3][i] == mat[4][i] && mat[4][i] != 0) {
                move(3, i, 4, i);
                k--;
            }
        }
    }
}
