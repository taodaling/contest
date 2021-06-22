package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.A__Colour_the_Flag;



import template.io.FastInput;
import template.io.FastOutput;

public class AColourTheFlag {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
        }
        char[][] cand = new char[n][m];
        for (int i = 0; i < 2; i++) {
            boolean ok = true;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    char c = (j + k) % 2 == i ? 'R' : 'W';
                    cand[j][k] = c;
                    if (mat[j][k] != '.' && mat[j][k] != cand[j][k]) {
                        ok = false;
                    }
                }
            }
            if (!ok) {
                continue;
            }
            out.println("YES");
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    out.append(cand[j][k]);
                }
                out.println();
            }
            return;
        }
        out.println("NO");
    }
}
