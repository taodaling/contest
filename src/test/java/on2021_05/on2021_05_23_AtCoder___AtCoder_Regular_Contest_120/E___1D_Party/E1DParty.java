package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.E___1D_Party;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class E1DParty {
    public int time(int l, int r) {
        return (r - l) >> 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        boolean[] ok = new boolean[n];
        IntPredicate predicate = m -> {
            Arrays.fill(ok, false);
            ok[0] = time(a[0], a[1]) <= m;
            if (n > 2) {
                ok[1] = time(a[0], a[2]) <= m;
            }
            for (int i = 2; i < n - 1; i++) {
                for (int j = i - 2; j >= i - 3 && j >= 0; j--) {
                    if(ok[j] && time(a[j], a[i + 1]) <= m){
                        ok[i] = true;
                    }
                }
            }
            boolean possible = false;
            if (ok[n - 2]) {
                possible = true;
            }
            if (n > 2 && ok[n - 3] && time(a[n - 3], a[n - 1]) <= m) {
                possible = true;
            }
            return possible;
        };
        //predicate.test(44);
        int first = BinarySearch.firstTrue(predicate, 0, (int) 1e9);
        out.println(first);
    }
}
