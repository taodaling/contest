package on2021_07.on2021_07_26_Codeforces___Codeforces_Global_Round_15.I__Organizing_a_Music_Festival;



import template.datastructure.PQTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;

import java.util.Arrays;

public class IOrganizingAMusicFestival {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        PQTree pqt = new PQTree(n);
        boolean[] set = new boolean[n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(set, false);
            int q = in.ri();
            for (int j = 0; j < q; j++) {
                set[in.ri() - 1] = true;
            }
            pqt.update(set);
        }
        if (!pqt.possible()) {
            out.println(0);
            return;
        }
        int[] res = pqt.getCounts();
        Factorial fact = new Factorial(n, mod);
        long ans = 1;
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i]; j++) {
                ans = ans * fact.fact(i) % mod;
            }
        }
        out.println(ans);
    }
}
