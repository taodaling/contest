package on2021_08.on2021_08_02_Codeforces___Codeforces_Round__736__Div__1_.C__The_Three_Little_Pigs0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;

public class CTheThreeLittlePigs {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 3e6 + 10, mod);

    public int[] div(int[] a, int[] b) {
        int[] res = new int[a.length];
        for (int i = a.length - 1; i >= (b.length - 1); i--) {
            int wpos = i - (b.length - 1);
            res[wpos] = a[i];
            for (int j = 0; j < b.length; j++) {
                a[i - j] = DigitUtils.mod(a[i - j] - (long) res[wpos] * b[b.length - 1 - j], mod);
            }
        }
        return res;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] numerator = new int[3 * (n + 1) + 1];
        for (int i = 0; i <= 3 * (n + 1); i++) {
            numerator[i] = comb.combination(3 * (n + 1), i);
        }
        int[] denominator = new int[3 + 1];
        for (int i = 0; i <= 3; i++) {
            numerator[i] -= comb.combination(3, i);
            if (numerator[i] < 0) {
                numerator[i] += mod;
            }
            denominator[i] += comb.combination(3, i);
        }
        denominator[0]--;
        if (denominator[0] < mod) {
            denominator[0] += mod;
        }
        int[] res = div(numerator, denominator);
        for (int i = 0; i < q; i++) {
            int x = in.ri();
            out.println(res[x]);
        }
    }
}
