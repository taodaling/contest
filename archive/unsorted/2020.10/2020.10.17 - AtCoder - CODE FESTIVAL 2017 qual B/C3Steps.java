package contest;

import template.datastructure.DeltaDSU;
import template.datastructure.XorDeltaDSU;
import template.io.FastInput;

import java.io.PrintWriter;

public class C3Steps {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        dsu.reset();
        boolean odd = false;
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            if (dsu.find(a) == dsu.find(b)) {
                if (dsu.delta(a, b) == 0) {
                    odd = true;
                }
                continue;
            }
            dsu.merge(a, b, 1);
        }
        if (odd) {
            long ans = (long)n * (n - 1) / 2;
            out.println(ans - m);
            return;
        }
        int[] cnt = new int[2];
        for(int i = 0; i < n; i++){
            cnt[dsu.delta(0, i)]++;
        }
        long ans = (long)cnt[0] * cnt[1];
        out.println(ans - m);
    }
}
