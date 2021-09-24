package on2021_07.on2021_07_05_DMOJ.Canada_Day_Contest_2021___0_1;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.ModMatrix;

public class CanadaDayContest202101 {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int k = in.ri();
        char[] a = new char[n];
        char[] b = new char[n];
        in.rs(a);
        in.rs(b);
        long[][] mat = new long[n + 1][n + 1];
        long totalWay = comb.invCombination(n, x);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= x && j <= i; j++) {
                int t = x - j;
                if (t > n - i) {
                    continue;
                }
                mat[i - j + t][i] = totalWay * comb.combination(i, j) % mod
                        * comb.combination(n - i, t) % mod;
            }
        }
        ModMatrix matrix = new ModMatrix((i, j) -> (int) mat[i][j], n + 1, n + 1);
        ModMatrix transform = ModMatrix.pow(matrix, k, mod);
        ModMatrix initState = new ModMatrix(n + 1, 1);
        int diff = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) {
                diff++;
            }
        }
        initState.set(diff, 0, 1);
        ModMatrix finalState = ModMatrix.mul(transform, initState, mod);
        int ans = finalState.get(0, 0);
        out.println(ans);
    }
}
