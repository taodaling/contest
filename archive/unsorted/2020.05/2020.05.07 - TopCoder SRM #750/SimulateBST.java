package contest;

import template.math.CachedPow;
import template.math.Modular;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class SimulateBST {
    //Debug debug = new Debug(true);
    public int checksum(int n, int[] Sprefix, int a, int m) {
        int[] s = Arrays.copyOf(Sprefix, n);
        int[] d = new int[n];
        int p = Sprefix.length;

        Modular mod = new Modular(1e9 + 7);
        CachedPow pow = new CachedPow(10, mod);
        int ans = 0;
        for (int i = 0; i < p; i++) {
            d[i] = add(s[i]);
            ans = mod.plus(ans, mod.mul(d[i], pow.pow(5 * i)));
        }
        Modular sMod = new Modular(m);
        for (int i = p; i < n; i++) {
            s[i] = sMod.valueOf((long) a * s[i - p] + (long) d[i - 1] + 1);
            d[i] = add(s[i]);
            ans = mod.plus(ans, mod.mul(d[i], pow.pow(5 * i)));
        }

       // debug.debug("s", s);
      //  debug.debug("d", d);
        return ans;
    }

    TreeMap<Integer, Integer> map = new TreeMap<>();

    public int add(Integer x) {
        if (map.isEmpty()) {
            map.put(x, 0);
            return 0;
        }
        Map.Entry<Integer, Integer> floor = map.floorEntry(x);
        Map.Entry<Integer, Integer> ceil = map.ceilingEntry(x);
        if (floor != null && floor.getKey().equals(x)) {
            return floor.getValue();
        }
        int depth;
        if (ceil == null || floor != null && floor.getValue().compareTo(ceil.getValue()) > 0) {
            depth = floor.getValue();
        } else {
            depth = ceil.getValue();
        }
        depth++;
        map.put(x, depth);
        return depth;
    }
}
