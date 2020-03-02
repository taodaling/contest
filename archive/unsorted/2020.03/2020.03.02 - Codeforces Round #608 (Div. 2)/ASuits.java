package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASuits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int d = in.readInt();
        long e = in.readInt();
        long f = in.readInt();

        int t1 = min(a, d);
        int t2 = min(b, c, d);

        long ans1 = t1 * e + min(b, c, d - t1) * f;
        long ans2 = min(a, d - t2) * e + t2 * f;
        out.println(Math.max(ans1, ans2));
    }


    public int min(int... x) {
        int ans = x[0];
        for (int y : x) {
            ans = Math.min(ans, y);
        }
        return ans;
    }
}
