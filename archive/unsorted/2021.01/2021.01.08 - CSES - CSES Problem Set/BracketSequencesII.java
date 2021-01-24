package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class BracketSequencesII {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[(int) 1e6];
        int k = in.rs(s, 0);
        if (n % 2 == 1) {
            out.println(0);
            return;
        }
        int sum = 0;
        int half = n / 2;
        int a = half;
        int b = half;
        for (int i = 0; i < k; i++) {
            if (s[i] == '(') {
                sum++;
                a--;
            } else {
                sum--;
                b--;
            }
            if (sum < 0) {
                out.println(0);
                return;
            }
        }
        if(a < 0 || b < 0){
            out.println(0);
            return;
        }
        int ans = comb.combination(a + b, a) - comb.combination(a + b, a + 1 + sum);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
