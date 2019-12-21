package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() - 1;
        }
        PreSum ps = new PreSum(a);
        LongHashMap map = new LongHashMap(n + 1, true);
        map.put(0, 1);
        int head = 0;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            while (head < i - k + 1) {
                long p = ps.prefix(head) % k;
                map.put(p, map.get(p) - 1);
                head++;
            }
            if(i == k - 1){
                map.put(0, map.get(0) - 1);
            }
            long p = ps.prefix(i) % k;
            long cnt = map.getOrDefault(p, 0);
            ans += cnt;
            map.put(p, map.getOrDefault(p, 0) + 1);
        }
        out.println(ans);
    }
}
