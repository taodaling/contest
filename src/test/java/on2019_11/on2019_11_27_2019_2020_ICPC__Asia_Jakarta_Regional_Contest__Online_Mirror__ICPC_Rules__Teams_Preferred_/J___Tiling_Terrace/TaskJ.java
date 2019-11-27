package on2019_11.on2019_11_27_2019_2020_ICPC__Asia_Jakarta_Regional_Contest__Online_Mirror__ICPC_Rules__Teams_Preferred_.J___Tiling_Terrace;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskJ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();

        char[] row = new char[n + 1];
        in.readString(row, 1);
        int[][] dp = new int[n + 1][51];
        SequenceUtils.deepFill(dp, (int) -1e9);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= 50; j++) {
                dp[i][j] = dp[i - 1][j];
                if (i >= 2 && row[i] == '.' && row[i - 1] == '.') {
                    dp[i][j] = Math.max(dp[i - 2][j] + b, dp[i][j]);
                }
                if (i >= 3 && j > 0 && row[i] == '.' && row[i - 1] == '#' && row[i - 2] == '.') {
                    dp[i][j] = Math.max(dp[i - 3][j - 1] + c, dp[i][j]);
                }
            }
        }

        int soilCnt = 0;
        for(int i = 1; i <= n; i++){
            if(row[i] == '.'){
                soilCnt++;
            }
        }

        int ans = 0;
        for (int i = 0; i <= 50; i++) {
            int bCnt = (dp[n][i] - i * c) / b;
            int bProfit = b;
            int cCnt = i;
            int cProfit = c;
            if(bProfit > c){
                int tmp = bCnt;
                bCnt = cCnt;
                cCnt = tmp;

                tmp = bProfit;
                bProfit = cProfit;
                cProfit = tmp;
            }

            int remain = soilCnt - (bCnt + cCnt) * 2;
            int total = dp[n][i];
            int used = Math.min(remain, k);
            total += used * a;
            ans = Math.max(ans, total);
            while(used < k && bCnt + cCnt > 0){
                if(bCnt > 0){
                    bCnt--;
                    int cost = Math.min(k - used, 2);
                    total += cost * a - bProfit;
                    used += cost;
                }else {
                    cCnt--;
                    int cost = Math.min(k - used, 2);
                    total += cost * a - cProfit;
                    used += cost;
                }
                ans = Math.max(ans, total);
            }
        }

        out.println(ans);
    }
}
