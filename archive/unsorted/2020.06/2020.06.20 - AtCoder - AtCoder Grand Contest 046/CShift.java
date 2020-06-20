package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.ArrayIndex;
import template.utils.Debug;

import java.util.Arrays;

public class CShift {
    Modular mod = new Modular(998244353);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
//        long max = 0;
//        for (int i = 0; i <= 300; i++) {
//            max = Math.max(i * i * i * (300 - i), max);
//        }
//        out.println(max);


        char[] s = (in.readString() + "0").toCharArray();
        int n = s.length;
        int k = in.readInt();


        IntegerList cntList = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            int j = i;
            while (j < n && s[j] == '1') {
                j++;
            }
            cntList.add(j - i);
            i = j;
        }

        int m = cntList.size();
        int[] cnts = cntList.toArray();
        int sum = Arrays.stream(cnts).sum();
        debug.debug("cnts", cnts);
        debug.debug("m", m);
        debug.debug("sum", sum);

        int[][][] dp = new int[m + 1][sum + 1][sum + 1];
        dp[0][0][0] = 1;
        int ps = 0;
        for (int i = 0; i < m; i++) {
            int origin = cnts[i];
            for (int j = ps; j <= sum; j++) {
                for (int t = 0; t <= j; t++) {
                    int plus = dp[i][j][t];
                    if (plus == 0) {
                        continue;
                    }
                    for (int x = 0; x + j <= sum; x++) {
                        if (x <= origin) {
                            dp[i + 1][j + x][t] = mod.plus(dp[i + 1][j + x][t], plus);
                        } else {
                            dp[i + 1][j + x][t + x - origin] = mod.plus(dp[i + 1][j + x][t + x - origin], plus);
                        }
                    }
                }
            }
            ps += cnts[i];
        }

        k = Math.min(k, sum);
        int ans = 0;
        for(int i = 0; i <= k; i++){
            ans = mod.plus(ans, dp[m][sum][i]);
        }

        debug.debug("dp", dp);
        debug.debug("dp[m][sum]", dp[m][sum]);

        out.println(ans);
    }
}
