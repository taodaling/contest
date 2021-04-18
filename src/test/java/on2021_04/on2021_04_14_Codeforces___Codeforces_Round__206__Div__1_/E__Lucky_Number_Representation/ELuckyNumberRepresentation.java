package on2021_04.on2021_04_14_Codeforces___Codeforces_Round__206__Div__1_.E__Lucky_Number_Representation;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ELuckyNumberRepresentation {
    int lim = 43;
    int[][] sol = new int[7][lim];
    int[] lucky = new int[]{0, 4, 7};
    int[] allComp;

    {
        SequenceUtils.deepFill(sol, -1);
        sol[0][0] = 0;
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j < lim; j++) {
                if (sol[i][j] == -1) {
                    continue;
                }
                for (int x : lucky) {
                    if (j + x < lim) {
                        sol[i + 1][j + x] = x;
                    }
                }
            }
        }
        IntegerArrayList possible = new IntegerArrayList();
        for (int i = 0; i < lim; i++) {
            if (sol[6][i] >= 0) {
                possible.add(i);
            }
        }
        allComp = possible.toArray();
    }

    int[][] dp = new int[20][5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        char[] s = Long.toString(n).toCharArray();
        int len = s.length;
        SequenceUtils.deepFill(dp, -1);
        dp[0][0] = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 5; j++) {
                if (dp[i][j] == -1) {
                    continue;
                }
                int sum = j * 10 + s[i] - '0';
                for (int k : allComp) {
                    int r = sum - k;
                    if (r < 0 || r >= 5) {
                        continue;
                    }
                    dp[i + 1][r] = j;
                }
            }
        }

        if (dp[len][0] == -1) {
            out.println(-1);
            return;
        }
        long[] built = new long[6];
        int cur = 0;
        long base = 1;
        for (int i = len; i > 0; i--, base *= 10) {
            int from = dp[i][cur];
            int sum = from * 10 + s[i - 1] - '0';
            int sub = sum - cur;
            cur = from;

            for (int j = 6; j > 0; j--) {
                built[j - 1] += base * sol[j][sub];
                sub -= sol[j][sub];
            }
        }

        for(long x : built){
            out.append(x).append(' ');
        }
        out.println();
    }
}
