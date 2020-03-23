package on2020_03.on2020_03_23_Educational_Codeforces_Round_84__Rated_for_Div__2_.D__Infinite_Path;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.List;

public class DInfinitePath {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = in.readInt();
        }
        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(p);
        List<IntegerList> circles = pp.extractCircles(1);
        int ans = n;
        for (IntegerList circle : circles) {
            int k = circle.size();
            debug.debug("circle", circle);
            IntegerList factors = Factorization.factorizeNumber(k);
            for (int i = 0; i < factors.size(); i++) {
                int f = factors.get(i);
                if (f > ans) {
                    continue;
                }
                boolean valid = false;
                for (int j = 0; j < f; j++) {
                    boolean local = true;
                    int now = c[circle.get(j)];
                    for (int t = (j + f) % k; t != j; t = (t + f) % k) {
                        if (now != c[circle.get(t)]) {
                            local = false;
                            break;
                        }
                    }
                    if (local) {
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    ans = Math.min(ans, f);
                }
            }
        }

        out.println(ans);
    }
}
