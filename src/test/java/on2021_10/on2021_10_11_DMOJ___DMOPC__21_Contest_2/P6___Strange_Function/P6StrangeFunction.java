package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P6___Strange_Function;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P6StrangeFunction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        p = in.ri(n);
        invP = new int[n];
        for (int i = 0; i < n; i++) {
            p[i]--;
            invP[p[i]] = i;
        }

        RangeTree rt = new RangeTree(n);
        boolean[] set = new boolean[n];
        int invoke = 0;
        for (int i = 0; i < q; i++) {
            int c = in.ri();
            if (c == 1) {
                invoke++;
                if (invoke > n) {
                    continue;
                }
                Arrays.fill(set, false);
                int iter = 0;
                for (int j = 0; j < n; j++) {
                    while (set[iter]) {
                        iter++;
                    }
                    int firstUnset = iter;
                    int mid = invP[firstUnset];
                    int l = j;
                    int r = mid;
                    for (int t = r - 1; t >= l; t--) {
                        set[p[t]] = true;
                        swap(t, t + 1);
                    }
                    set[p[l]] = true;
                    j = r;
                }
            } else if (c == 2) {
                int x = in.ri() - 1;
                out.println(p[x] + 1);
            } else {
                int x = in.ri() - 1;
                out.println(invP[x] + 1);
            }
        }
    }

    int[] p;
    int[] invP;

    public void swap(int i, int j) {
        int tmp = p[i];
        p[i] = p[j];
        p[j] = tmp;
        invP[p[i]] = i;
        invP[p[j]] = j;
    }
}
