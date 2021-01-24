package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitCount;
import template.math.DigitUtils;

public class BracketSequencesI {
    int mod = (int)1e9 + 7;
    Combination comb = new Combination((int)1e6, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if(n % 2 == 1){
            out.println(0);
            return;
        }
        int ans = comb.combination(n, n / 2) - comb.combination(n, n / 2 + 1);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
