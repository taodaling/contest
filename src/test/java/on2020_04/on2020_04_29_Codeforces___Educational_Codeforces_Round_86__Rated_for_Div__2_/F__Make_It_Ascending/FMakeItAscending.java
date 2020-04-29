package on2020_04.on2020_04_29_Codeforces___Educational_Codeforces_Round_86__Rated_for_Div__2_.F__Make_It_Ascending;



import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

public class FMakeItAscending {
    int inf = (int) 1e9;
    int[][][] dp = new int[16][15][1 << 15];
    int[][][] lastJ = new int[16][15][1 << 15];
    int[][][] lastS = new int[16][15][1 << 15];
    int[] a = new int[15];
    int[] sum = new int[1 << 15];
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        int mask = (1 << n) - 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= mask; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }

        sum[0] = 0;
        for (int i = 1; i <= mask; i++) {
            int log = Log2.floorLog(Integer.lowestOneBit(i));
            sum[i] = sum[i - (1 << log)] + a[log];
        }

        int ansI = -1;
        int ansJ = -1;

        for (int i = n; i >= 1 && ansI == -1; i--) {
            for (int j = 0; j < n && ansI == -1; j++) {
                if (dp(i, j, mask - (1 << j)) == inf) {
                    continue;
                }
                ansI = i;
                ansJ = j;
            }
        }

        out.println(n - ansI);
        int s = mask - (1 << ansJ);
        IntegerBIT bit = new IntegerBIT(n);
        for (int i = 1; i <= n; i++) {
            bit.update(i, 1);
        }
        while (ansI > 0) {
            int ls = lastS[ansI][ansJ][s];
            int lj = lastJ[ansI][ansJ][s];
            int x = s - ls;
            if (ansI > 1) {
                x -= (1 << lj);
            }
            if (s < 0) {
                throw new RuntimeException();
            }
            debug.debug("j", ansJ);
            debug.debug("s", Integer.toString(s, 2));
            for (int i = 0; i < n; i++) {
                if (Bits.bitAt(x, i) == 0) {
                    continue;
                }
                out.append(bit.query(i + 1)).append(' ').append(bit.query(ansJ + 1)).println();
                bit.update(i + 1, -1);
            }
            s = ls;
            ansJ = lj;
            ansI--;
        }
    }

    public int dp(int i, int j, int k) {
        if (i == 0) {
            return k == 0 ? 0 : inf;
        }
        if (dp[i][j][k] == -1) {
            dp[i][j][k] = inf;
            lastJ[i][j][k] = -1;
            lastS[i][j][k] = -1;
            if (Integer.bitCount(k) + 1 < i || Bits.bitAt(k, j) == 1) {
                return dp[i][j][k];
            }
            if (i == 1) {
                lastS[i][j][k] = 0;
                lastJ[i][j][k] = 0;
                return dp[i][j][k] = sum[k + (1 << j)];
            }
            if (j == 0) {
                return dp[i][j][k];
            }
            for (int t = i - 2; t < j; t++) {
                if (Bits.bitAt(k, t) == 0) {
                    continue;
                }
                int remain = k - (1 << t);
                for (int x = remain; ; x = (x - 1) & remain) {
                    int s = sum[(remain - x) | (1 << j)];
                    if (!(s >= dp[i][j][k] || dp(i - 1, t, x) >= s)) {
                        dp[i][j][k] = s;
                        lastJ[i][j][k] = t;
                        lastS[i][j][k] = x;
                    }
                    if (x == 0) {
                        break;
                    }
                }
            }
        }
        return dp[i][j][k];
    }
}
