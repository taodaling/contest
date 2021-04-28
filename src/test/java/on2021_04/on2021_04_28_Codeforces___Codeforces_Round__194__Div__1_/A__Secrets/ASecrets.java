package on2021_04.on2021_04_28_Codeforces___Codeforces_Round__194__Div__1_.A__Secrets;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class ASecrets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        while(n % 3 == 0){
            n /= 3;
        }
        out.println(DigitUtils.ceilDiv(n, 3));
    }
}
