package on2021_11.on2021_11_13_AtCoder___AtCoder_Grand_Contest_055.A___ABC_Identity;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.stream.IntStream;

public class AABCIdentity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        IntegerDeque[][] dqs = new IntegerDeque[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                dqs[i][j] = new IntegerDequeImpl(n);
            }
        }
        int[] assign = new int[n * 3];
        for (int r = 0; r < 3; r++) {
            for (int i = 0; i < n; i++) {
                char c = in.rc();
                int v = c == 'A' ? 0 : c == 'B' ? 1 : 2;
                dqs[r][v].addLast(i + r * n);
            }
        }
        int[] perm = IntStream.range(0, 3).toArray();
        for (int i = 0; i < n; i++) {
            Arrays.sort(perm);
            int index = 0;
            do {
                boolean ok = true;
                for (int j = 0; j < 3; j++) {
                    if (dqs[j][perm[j]].isEmpty()) {
                        ok = false;
                    }
                }
                if (ok) {
                    for (int j = 0; j < 3; j++) {
                        int pop = dqs[j][perm[j]].removeFirst();
                        assign[pop] = index;
                    }
                }
                index++;
            } while (PermutationUtils.nextPermutation(perm));
        }
        for(int i = 0; i < assign.length; i++){
            out.append(assign[i] + 1);
        }
    }

}
