package contest;

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
