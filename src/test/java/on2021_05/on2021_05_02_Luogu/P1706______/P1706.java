package on2021_05.on2021_05_02_Luogu.P1706______;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class P1706 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();

        int[] perm = IntStream.range(1, n + 1).toArray();
        int[] rev = IntStream.range(1, n + 1).toArray();
        for (int i = 0; i < rev.length; i++) {
            rev[i] = -rev[i];
        }
        int len = 1;
        for (int i = 0; i < n; i++) {
            len = (i + 1) * len;
        }
        for (int i = 1; i < len; i++) {
            PermutationUtils.nextPermutation(perm);
            PermutationUtils.prevPermutation(rev);
            for (int j = 0; j < n; j++) {
                if (perm[j] != -rev[j]) {
                    throw new RuntimeException();
                }
            }
        }
    }
}
