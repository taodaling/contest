package on2020_02.on2020_02_08_Codeforces_Round__578__Div__2_.D__White_Lines;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT2D;

public class DWhiteLines {
    int n;
    int[][] tags;

    public void add(int l, int r, int t, int b) {
        l = Math.max(l, 1);
        r = Math.min(r, n);
        t = Math.max(t, 1);
        b = Math.min(b, n);
        if (l > r || t > b) {
            return;
        }
        tags[t][l] += 1;
        if (b < n) {
            tags[b + 1][l] -= 1;
        }
        if (r < n) {
            tags[t][r + 1] -= 1;
        }
        if (b < n && r < n) {
            tags[b + 1][r + 1] += 1;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int k = in.readInt();
        tags = new int[n + 1][n + 1];
        int[][] mat = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                mat[i][j] = in.readChar() == 'B' ? 1 : 0;
            }
        }
        for (int i = 1; i <= n; i++) {
            int l = n + 1;
            int r = 0;
            for (int j = 1; j <= n; j++) {
                if (mat[i][j] == 1) {
                    l = Math.min(l, j);
                    r = Math.max(r, j);
                }
            }
            if (l > r) {
                tags[1][1] += 1;
                continue;
            }
            add(r - k + 1, l, i - k + 1, i);
        }

        for (int i = 1; i <= n; i++) {
            int l = n + 1;
            int r = 0;
            for (int j = 1; j <= n; j++) {
                if (mat[j][i] == 1) {
                    l = Math.min(l, j);
                    r = Math.max(r, j);
                }
            }
            if (l > r) {
                tags[1][1] += 1;
                continue;
            }
            add(i - k + 1, i, r - k + 1, l);
        }

        for (int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++){
                tags[i][j] += tags[i - 1][j] +
                        tags[i][j - 1] -
                        tags[i - 1][j - 1];
            }
        }

        int ans = 0;
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j++){
                ans = Math.max(tags[i][j], ans);
            }
        }

        out.println(ans);
    }
}
