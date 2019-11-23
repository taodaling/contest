package on2019_11.on2019_11_23_Hello_2019.F___Alex_and_a_TV_Show;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);
        int limit = 1000000;
        int[] dp = new int[limit + 1];
        int[] cnts = new int[limit + 1];
        for(int i = 0; i < n; i++){
            cnts[in.readInt()]++;
        }
        for(int i = 1; i <= limit; i++){
            int cnt = 0;
            for(int j = i; j <= limit; j += i){
                cnt += cnts[j];
            }
            dp[i] = mod.subtract(pow.pow(2, cnt), 1);
        }
        for(int i = limit; i >= 1; i--){
            for(int j = i + i; j <= limit; j += i){
                dp[i] = mod.subtract(dp[i], dp[j]);
            }
        }
        out.println(dp[1]);
    }
}
