package on2020_07.on2020_07_19_Codeforces___Codeforces_Round__393__Div__1___8VC_Venture_Cup_2017___Final_Round_Div__1_Edition_.A__Pavel_and_barbecue;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;

import java.util.Arrays;

public class APavelAndBarbecue {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        int[] rev = new int[n];
        in.populate(p);
        in.populate(rev);
        for (int i = 0; i < n; i++) {
            p[i]--;
        }
        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(p);
        int c = pp.extractCircles().size();
        int change = 0;
        if (c > 1) {
            change += c;
        }
        int sum = Arrays.stream(rev).sum();
        change += 1 - sum % 2;
        out.println(change);
    }
}
