package on2021_06.on2021_06_26_AtCoder___AtCoder_Beginner_Contest_205.E___White_and_Black_Balls;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class EWhiteAndBlackBalls {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 3e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();
        int k = in.ri();
        if(m > n + k){
            out.println(0);
            return;
        }
        long ans = comb.combination(n + m, n) - comb.combination(n + m, n + 1 + k);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
