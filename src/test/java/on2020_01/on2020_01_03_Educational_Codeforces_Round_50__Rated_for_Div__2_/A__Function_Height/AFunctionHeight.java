package on2020_01.on2020_01_03_Educational_Codeforces_Round_50__Rated_for_Div__2_.A__Function_Height;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AFunctionHeight {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long k = in.readLong();
        long ans = DigitUtils.ceilDiv(k, n);
        out.println(ans);
    }
}
