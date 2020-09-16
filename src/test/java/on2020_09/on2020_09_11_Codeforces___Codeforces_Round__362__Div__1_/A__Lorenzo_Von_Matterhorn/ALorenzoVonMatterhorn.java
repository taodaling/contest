package on2020_09.on2020_09_11_Codeforces___Codeforces_Round__362__Div__1_.A__Lorenzo_Von_Matterhorn;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ALorenzoVonMatterhorn {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        Map<Long, Long> map = new HashMap<>();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            long u = in.readLong();
            long v = in.readLong();
            if (t == 1) {
                long w = in.readLong();
                while (u != v) {
                    if (u < v) {
                        long tmp = u;
                        u = v;
                        v = tmp;
                    }
                    map.put(u, map.getOrDefault(u, 0L) + w);
                    u /= 2;
                }
            } else {
                long ans = 0;
                while (u != v) {
                    if (u < v) {
                        long tmp = u;
                        u = v;
                        v = tmp;
                    }
                    ans += map.getOrDefault(u, 0L);
                    u /= 2;
                }
                out.println(ans);
            }
        }
    }
}
