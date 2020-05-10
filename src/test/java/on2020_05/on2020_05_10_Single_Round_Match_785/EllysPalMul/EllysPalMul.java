package on2020_05.on2020_05_10_Single_Round_Match_785.EllysPalMul;



import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.utils.SequenceUtils;

import java.math.BigInteger;

public class EllysPalMul {
    int[] buf = new int[30];

    int[][] dp = new int[20][100000];
    Modular mod;
    CachedPow pow;
    int n;


    public int mirror(int i) {
        return n - 1 - i;
    }

    public int getP(int x, int len) {
        this.n = len;
        SequenceUtils.deepFill(dp, 10);

        for (int i = (len - 1) / 2; i >= 0; i--) {
            int begin = 0;
            if (i == 0) {
                begin = 1;
            }
            int m = mirror(i);
            for (int j = begin; j < 10; j++) {
                int val = mod.mul(j, pow.pow(i));
                if (m != i) {
                    val = mod.plus(val, mod.mul(j, pow.pow(m)));
                }
                if (i == (len - 1) / 2) {
                    //no prev
                    if (dp[i][val] > j) {
                        dp[i][val] = j;
                    }
                } else {
                    for (int k = 0; k < x; k++) {
                        int next = mod.plus(k, val);
                        if (dp[i][next] > j && dp[i + 1][k] != 10) {
                            dp[i][next] = j;
                        }
                    }
                }
            }
        }

        if (dp[0][0] == 10) {
            return -1;
        }
        StringBuilder builder = new StringBuilder();
        trace(0, 0, builder);
        BigInteger ans = new BigInteger(builder.toString()).divide(BigInteger.valueOf(x));
        if(ans.compareTo(BigInteger.valueOf((long) 1e9)) > 0){
            return -1;
        }
        return ans.intValue();
    }

    public void trace(int i, int j, StringBuilder builder) {
        int m = mirror(i);
        if (m < i) {
            return;
        }
        builder.append(dp[i][j]);
        int val = mod.mul(dp[i][j], pow.pow(i));
        if (m != i) {
            val = mod.plus(val, mod.mul(dp[i][j], pow.pow(m)));
            trace(i + 1, mod.subtract(j, val), builder);
            builder.append(dp[i][j]);
        }
    }

    public int getMin(int X) {
        this.mod = new Modular(X);
        this.pow = new CachedPow(10, mod);

        if (X < 10) {
            return 1;
        }

        for(int i = 1; i <= 20; i++){
            int p = getP(X, i);
            if(p != -1){
                return p;
            }
        }

        return -1;
    }
}
