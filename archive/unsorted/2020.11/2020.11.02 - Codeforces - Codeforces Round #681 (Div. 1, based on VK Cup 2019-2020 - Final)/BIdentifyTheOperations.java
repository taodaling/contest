package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BIdentifyTheOperations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];
        in.populate(a);
        in.populate(b);
        int[] invIndex = new int[n + 1];
        Set<Integer> deleted = new HashSet<>();
        for (int i = 0; i < m; i++) {
            deleted.add(b[i]);
        }
        for (int i = 0; i < n; i++) {
            invIndex[a[i]] = i;
        }
        int mod = 998244353;
        long prod = 1;
        for (int x : b) {
            int index = invIndex[x];
            boolean left = index > 0 && !deleted.contains(a[index - 1]);
            boolean right = index + 1 < n && !deleted.contains(a[index + 1]);
            if (left && right) {
                prod = prod * 2 % mod;
            }
            if (!left && !right) {
                prod = 0;
            }
            deleted.remove(x);
        }
        out.println(prod);
    }
}
