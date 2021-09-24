package on2021_08.on2021_08_01_Codeforces___Codeforces_Round__736__Div__1_.D1__Gregor_and_the_Odd_Cows__Easy_;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class D1GregorAndTheOddCowsEasy {
    int sign(int x) {
        return (x >> 1) & 1;
    }

    public long pick(long n, long k) {
        return k == 0 ? 1 : pick(n - 1, k - 1) * n / k;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.ri();
            }
        }
        int[] g = new int[4];
        for (int i = 0; i < n; i++) {
            g[sign(pts[i][0]) << 1 | sign(pts[i][1])]++;
        }

        long[][] prev = new long[4][2];
        long[][] next = new long[4][2];
        prev[0][0] = 1;
        for (int x : g) {
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j < 4; j++) {
                for (int t = 0; t < 2; t++) {
                    if(prev[j][t] == 0){
                        continue;
                    }
                    for (int z = 0; z <= 3 && z + j < 4 && z <= x; z++) {
                        next[j + z][(t + j * z) & 1] += prev[j][t] * pick(x, z);
                    }
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = prev[3][0];
        out.println(ans);
    }
}
