package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class PermutationsII {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e3, mod);

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        //active
        //sign
        //segment
        long[][][] prev = new long[2][2][n + 1];
        long[][][] next = new long[2][2][n + 1];
        prev[0][0][1] = 1;
        for (int i = 1; i <= n; i++) {
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t <= n; t++) {
                        prev[j][k][t] = DigitUtils.mod(prev[j][k][t], mod);
                    }
                }
            }
            if(i == n){
                break;
            }
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t <= i; t++) {
                        long way = prev[j][k][t];
                        if (way == 0) {
                            continue;
                        }
                        //extend
                        next[1][k ^ 1][t] += way * (2 - j);
                        //new
                        next[0][k][t + 1] += way;
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }

        debug.debug("prev", prev);
        long ans = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k <= n; k++){
                    if(prev[i][j][k] == 0){
                        continue;
                    }
                    long contrib = prev[i][j][k] * fact.fact(k) % mod * (1 - 2 * j);
                    ans += contrib;
                }
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
