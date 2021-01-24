package contest;

import combinatorics.Permutations;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;

import java.util.stream.IntStream;

public class NationalFlag {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] perm = IntStream.range(0, 3).toArray();
        int[] bestPerm = null;
        int best = (int)1e9;
        do {
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                int offset = perm[i % 3];
                cnt += (m - 1 - offset) / 3 + 1;
            }
            if (cnt < best) {
                best = cnt;
                bestPerm = perm.clone();
            }
        } while (Permutations.nextPermutation(perm));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j % 3 == bestPerm[i % 3]) {
                    out.append('#');
                }else{
                    out.append('0');
                }
            }
            out.println();
        }
    }
}
