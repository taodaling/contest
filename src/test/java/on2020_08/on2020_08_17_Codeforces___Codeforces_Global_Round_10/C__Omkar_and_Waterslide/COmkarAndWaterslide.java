package on2020_08.on2020_08_17_Codeforces___Codeforces_Global_Round_10.C__Omkar_and_Waterslide;



import template.io.FastInput;
import template.io.FastOutput;

public class COmkarAndWaterslide {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] pre = new int[n];
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = Math.max(pre[i - 1], a[i]);
        }

        int last = 0;
        long ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (pre[i] == a[i]) {
                last = 0;
                continue;
            }
            int need = pre[i] - a[i];
            ans += Math.max(0, need - last);
            last = need;
        }
        out.println(ans);

    }
}
