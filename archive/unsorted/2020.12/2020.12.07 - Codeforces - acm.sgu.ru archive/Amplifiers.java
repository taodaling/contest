package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Amplifiers {
    Map<Integer, Sol> map = new HashMap<>();
    int inf = (int) 1e8;

    public int find(int k) {
        if(!map.containsKey(k)){
            Sol sol = new Sol();
            sol.step = inf;
            sol.next = -1;
            if (k == 1) {
                sol.step = 0;
            }else {
                if ((k + 1) % 2 == 0) {
                    int cand = find((k + 1) / 2) + 1;
                    if (cand < sol.step) {
                        sol.step = cand;
                        sol.next = (k + 1) / 2;
                    }
                }
                if ((k - 1) % 2 == 0) {
                    int cand = find((k - 1) / 2) + 1;
                    if (cand < sol.step) {
                        sol.step = cand;
                        sol.next = (k - 1) / 2;
                    }
                }
            }
            map.put(k, sol);
        }
        return map.get(k).step;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int ans = find(n);
        debug.debug("map", map);
        debug.debug("map.size()", map.size());
        if (ans >= inf) {
            out.println("No solution");
            return;
        }
        List<Integer> way = new ArrayList<>(ans);
        int now = n;
        while (now > 1) {
            assert map.containsKey(now);
            int next = map.get(now).next;
            if (next * 2 + 1 == now) {
                way.add(2);
            } else {
                way.add(1);
            }
            now = next;
        }

        SequenceUtils.reverse(way);
        out.println(way.size());
        for(int x : way){
            out.append(x).append(' ');
        }
    }
}

class Sol {
    int step;
    int next;
}
