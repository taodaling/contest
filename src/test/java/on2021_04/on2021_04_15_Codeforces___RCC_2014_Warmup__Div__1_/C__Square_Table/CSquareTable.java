package on2021_04.on2021_04_15_Codeforces___RCC_2014_Warmup__Div__1_.C__Square_Table;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

public class CSquareTable {
    int N = 100;
    int M = 1000;
    int[][] dp = new int[N + 1][M];

    {
        SequenceUtils.deepFill(dp, -1);
        dp[0][0] = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (dp[i][j] == -1) {
                    continue;
                }
                for (int k = 1; j + k * k < M; k++) {
                    dp[i + 1][j + k * k] = k;
                }
            }
        }
    }

    int[] build(int n) {
        IntegerArrayList ans = new IntegerArrayList(n);
        int sum = 0;
        for (int j = 1; j * j < M; j++) {
            if (dp[n][j * j] != -1) {
                sum = j * j;
                break;
            }
        }
        for (int i = n; i > 0; i--) {
            ans.add(dp[i][sum]);
            sum -= dp[i][sum] * dp[i][sum];
        }
        return ans.toArray();
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = build(n);
        int[] b = build(m);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(a[i] * b[j]).append(' ');
            }
            out.println();
        }
    }
}
