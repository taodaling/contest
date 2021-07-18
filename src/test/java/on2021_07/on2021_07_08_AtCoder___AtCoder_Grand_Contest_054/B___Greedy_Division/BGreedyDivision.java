package on2021_07.on2021_07_08_AtCoder___AtCoder_Grand_Contest_054.B___Greedy_Division;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitCount;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BGreedyDivision {
    int mod = 998244353;
    Factorial fact = new Factorial(1000, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] ws = in.ri(n);
        int sum = Arrays.stream(ws).sum();
        if(sum % 2 == 1){
            out.println(0);
            return;
        }
        int L = 5000;
        long[][] prev = new long[n][L + 1];
        long[][] next = new long[n][L + 1];
        prev[0][0] = 1;
        for(int w : ws){
            SequenceUtils.deepFill(next, 0L);
            for(int i = 0; i < n; i++){
                for(int j = 0; j <= L; j++){
                    //not take
                    next[i][j] += prev[i][j];
                    //take
                    if(i + 1 < n && j + w <= L){
                        next[i + 1][j + w] += prev[i][j];
                    }
                }
            }
            for(int i = 0; i < n; i++){
                for(int j = 0; j <= L; j++){
                    next[i][j] = DigitUtils.modWithoutDivision(next[i][j], mod);
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }

        int half = sum / 2;
        long ans = 0;
        for(int i = 0; i < n; i++){
            long contrib = prev[i][half] * fact.fact(i) % mod * fact.fact(n - i) % mod;
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
