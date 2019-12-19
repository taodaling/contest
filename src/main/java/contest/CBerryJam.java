package contest;

import template.datastructure.IntHashMap;
import template.datastructure.PreSum;
import template.io.FastInput;
import template.io.FastOutput;

public class CBerryJam {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() == 1 ? 1 : -1;
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt() == 1 ? 1 : -1;
        }

        IntHashMap map = new IntHashMap(n, false);
        PreSum psOfA = new PreSum(a);
        PreSum psOfB = new PreSum(b);

        int inf = (int) 1e8;
        for (int i = 0; i < n; i++) {
            map.put((int) psOfB.post(i), Math.min(i, map.getOrDefault((int) psOfB.post(i), inf)));
        }
        map.put(0, Math.min(n, map.getOrDefault(0, inf)));

        int ans = n * 2;
        for (int i = 0; i < n; i++) {
            int diff = (int) psOfA.prefix(i);
            ans = Math.min(ans, n - 1 - i + map.getOrDefault(-diff, inf));
        }
        ans = Math.min(ans, n + map.getOrDefault(0, inf));

        out.println(ans);
    }
}
