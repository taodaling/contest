package on2021_06.on2021_06_02_2021_TopCoder_Open_Algo.AlternateOddEven;



import template.math.LongRadix;
import template.utils.SequenceUtils;

public class AlternateOddEven {
    long[][][][] dp = new long[19][2][2][2];
    long x;
    LongRadix radix = new LongRadix(10);

    long dp(int i, int ceil, int start, int odd) {
        if (i < 0) {
            return start == 1 ? 1 : 0;
        }
        if (dp[i][ceil][start][odd] == -1) {
            long ans = 0;
            int v = radix.get(x, i);
            for (int j = 0; j < 10; j++) {
                if (start == 1 && odd != j % 2) {
                    continue;
                }
                if (ceil == 1 && j > v) {
                    continue;
                }
                ans += dp(i - 1, ceil == 1 && j == v ? 1 : 0, start == 1 || j > 0 ? 1 : 0, (j % 2) ^ 1);
            }
            dp[i][ceil][start][odd] = ans;
        }
        return dp[i][ceil][start][odd];
    }


    long count(long x) {
        SequenceUtils.deepFill(dp, -1L);
        this.x = x;
        long ans = dp(18, 1, 0, 0);
        return ans;
    }


    public long kth(long K) {
        count(9);

        long hi = (long)1e18;
        long lo = 1;
        while(lo < hi){
            long mid = (lo + hi) / 2;
            if(count(mid) >= K){
                hi = mid;
            }else{
                lo = mid + 1;
            }
        }
        return hi;
    }
}
