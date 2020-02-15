package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerSparseTable;

public class AMindControl {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        k = Math.min(k, m - 1);

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        int ans = 0;
        int unknown = m - 1 - k;
        for (int i = 0; i <= k; i++) {
            int l = i;
            int local = (int) 1e9;
            for (int j = l; j <= l + unknown; j++) {
                int head = j;
                int tail = n - 1 - (m - 1 - head);
                local = Math.min(local, Math.max(a[head], a[tail]));
            }
            ans = Math.max(ans, local);
        }

        out.println(ans);
    }
}
