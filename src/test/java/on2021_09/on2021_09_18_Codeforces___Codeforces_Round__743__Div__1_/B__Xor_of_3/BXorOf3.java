package on2021_09.on2021_09_18_Codeforces___Codeforces_Round__743__Div__1_.B__Xor_of_3;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BXorOf3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.ri(n);
        for (int i = 2; i < n; i++) {
            if (sum(i - 2, i) == 2) {
                //del

            }
        }
    }

    int[] a;
    List<int[]> sol = new ArrayList<>();

    public int sum(int l, int r) {
        int ans = 0;
        for (int i = l; i <= r; i++) {
            ans += a[i];
        }
        return ans;
    }

    public void apply(int l, int r) {
        assert r - l + 1 == 3;
        int xor = 0;
        for (int i = l; i <= r; i++) {
            xor ^= a[i];
        }
        for (int i = l; i <= r; i++) {
            a[i] = xor;
        }
        sol.add(new int[]{l, r});
    }
}
