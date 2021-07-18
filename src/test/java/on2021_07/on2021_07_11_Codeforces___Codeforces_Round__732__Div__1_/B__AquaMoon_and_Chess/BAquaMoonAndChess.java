package on2021_07.on2021_07_11_Codeforces___Codeforces_Round__732__Div__1_.B__AquaMoon_and_Chess;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

public class BAquaMoonAndChess {
    int mod = 998244353;
    Combination comb = new Combination((int) 2e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        int empty = 0;
        int block = 0;
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i;
            while (r + 1 < n && s[r + 1] == s[r]) {
                r++;
            }
            i = r;
            if (s[l] == '0') {
                empty += r - l + 1;
            } else {
                block += DigitUtils.floorDiv(r - l + 1, 2);
            }
        }
        long ans = comb.combination(empty + block, empty);
        out.println(ans);
    }
}
