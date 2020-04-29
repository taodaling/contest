package on2020_04.on2020_04_29_Codeforces___Codeforces_Round__445__Div__1__based_on_Technocup_2018_Elimination_Round_3_.E__Mod_Mod_Mod;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Map;
import java.util.TreeMap;

public class EModModMod {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        long[] a = new long[n + 1];
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
        }
        a[n] = 1;
        n++;

        TreeMap<Long, Long>[] dp = new TreeMap[n];
        for (int i = 0; i < n; i++) {
            dp[i] = new TreeMap<>();
        }
        dp[0].put(a[0] - 1, 0L);
        for (int i = 0; i < n - 1; i++) {
            long next = a[i + 1];
            for (Map.Entry<Long, Long> entry : dp[i].entrySet()) {
                long cur = entry.getKey();
                //[0, cur % next]
                {
                    long plus = (cur / next) * next;
                    add(dp[i + 1], cur % next, plus * (i + 1) + entry.getValue());
                }
                //[0, next - 1]
                {
                    long plus = ((cur - next) / next) * next;
                    add(dp[i + 1], Math.min(cur, next - 1), plus * (i + 1) + entry.getValue());
                }
            }
        }


        debug.debug("dp", dp);
        long ans = dp[n - 1].firstEntry().getValue();
        out.println(ans);
    }

    public void add(TreeMap<Long, Long> map, Long key, Long value) {
        //strictly decrease
        while (true) {
            Map.Entry<Long, Long> ceil = map.ceilingEntry(key);
            if (ceil == null) {
                break;
            }
            if (ceil.getValue() >= value) {
                return;
            }
            break;
        }
        while (true) {
            Map.Entry<Long, Long> floor = map.floorEntry(key);
            if (floor == null) {
                break;
            }
            if (floor.getValue() <= value) {
                map.remove(floor.getKey());
            } else {
                break;
            }
        }

        map.put(key, value);
    }
}

class Segment {
    long val;
    int cnt;
}