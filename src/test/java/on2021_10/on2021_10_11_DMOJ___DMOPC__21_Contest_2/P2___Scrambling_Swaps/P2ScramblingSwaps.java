package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P2___Scrambling_Swaps;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.stream.IntStream;

public class P2ScramblingSwaps {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        perm = IntStream.range(0, n).toArray();
        invPerm = perm.clone();

        for (int i = 0; i < q; i++) {
            char c = in.rc();
            if (c == 'B') {
                //swap val
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                swap(invPerm[a], invPerm[b]);
            } else if (c == 'E') {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                swap(a, b);
            } else {
                int[] p = in.ri(n);
                for (int j = 0; j < n; j++) {
                    p[j]--;
                }
                int[] res = new int[n];
                for (int j = 0; j < n; j++) {
                    res[j] = p[invPerm[j]];
                }
                for (int j = 0; j < n; j++) {
                    out.append(res[j] + 1);
                    if (j + 1 < n) {
                        out.append(' ');
                    }
                }
                out.println();
            }
        }
    }

    int[] perm;
    int[] invPerm;

    public void swap(int i, int j) {
        int tmp = perm[i];
        perm[i] = perm[j];
        perm[j] = tmp;

        invPerm[perm[i]] = i;
        invPerm[perm[j]] = j;
    }
}
