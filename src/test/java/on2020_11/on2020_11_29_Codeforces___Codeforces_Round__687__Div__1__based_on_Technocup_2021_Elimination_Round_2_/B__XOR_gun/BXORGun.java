package on2020_11.on2020_11_29_Codeforces___Codeforces_Round__687__Div__1__based_on_Technocup_2021_Elimination_Round_2_.B__XOR_gun;



import template.datastructure.PreXor;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BXORGun {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        if (n > 100) {
            out.println(1);
            return;
        }
        int ans = inf;
        PreXor xor = new PreXor(a);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                for (int t = j + 1; t < n; t++) {
                    for (int k = t; k < n; k++) {
                        if (xor.intervalSum(i, j) > xor.intervalSum(t, k)) {
                            ans = Math.min(ans, j - i + k - t);
                        }
                    }
                }
            }
        }
        out.println(ans == inf ? -1 : ans);
    }

    int inf = (int) 1e9;

}
