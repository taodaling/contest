package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

public class BNiceShopping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.readInt();
        int B = in.readInt();
        int M = in.readInt();
        int[] a = new int[A + 1];
        for (int i = 1; i <= A; i++) {
            a[i] = in.readInt();
        }
        int[] b = new int[B + 1];
        for (int i = 1; i <= B; i++) {
            b[i] = in.readInt();
        }
        long ans = CompareUtils.minOf(a, 1, A) + CompareUtils.minOf(b, 1, B);
        for (int i = 0; i < M; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int c = in.readInt();
            ans = Math.min(ans, a[x] + b[y] - c);
        }
        out.println(ans);
    }
}
