package on2021_04.on2021_04_18_AtCoder___AtCoder_Regular_Contest_117.C___Tricolor_Pyramid;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.IntCombination;
import template.math.Lucas;

public class CTricolorPyramid {
    int mod = 3;
    IntCombination comb = new Lucas(new Combination(mod, mod), mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            char c = in.rc();
            int v = 0;
            if (c == 'B') {
                v = 0;
            } else if (c == 'W') {
                v = 1;
            } else {
                v = 2;
            }
            long way = comb.combination(n - 1, i);
            sum += way * v;
        }
        if(n % 2 == 0){
            sum = -sum;
        }
        sum = DigitUtils.mod(sum, mod);
        out.println("BWR".charAt((int)sum));
    }
}
