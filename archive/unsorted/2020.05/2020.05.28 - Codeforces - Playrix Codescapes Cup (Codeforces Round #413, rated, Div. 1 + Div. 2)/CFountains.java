package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CFountains {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int c = in.readInt();
        int d = in.readInt();

        TreeMap<Integer, Integer> incC = new TreeMap<>();
        TreeMap<Integer, Integer> incD = new TreeMap<>();

        int ans = 0;
        List<Fountain> cList = new ArrayList<>(n);
        List<Fountain> dList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Fountain f = new Fountain();
            f.b = in.readInt();
            f.p = in.readInt();
            if (in.readChar() == 'C') {
                cList.add(f);
            } else {
                dList.add(f);
            }
        }

        ans = Math.max(ans, maxBeauty(incC, cList, c));
        ans = Math.max(ans, maxBeauty(incD, dList, d));
        debug.debug("incC", incC);
        debug.debug("incD", incD);

        if (incC.floorEntry(c) != null && incD.floorEntry(d) != null) {
            ans = Math.max(ans, incC.floorEntry(c).getValue() + incD.floorEntry(d).getValue());
        }

        out.println(ans);
    }

    public int maxBeauty(TreeMap<Integer, Integer> map, List<Fountain> list, int limit) {
        int ans = 0;
        for (Fountain f : list) {
            Map.Entry<Integer, Integer> floor = map.floorEntry(limit - f.p);
            if (floor != null) {
                ans = Math.max(ans, f.b + floor.getValue());
            }

            floor = map.floorEntry(f.p);
            if (floor != null && floor.getValue() >= f.b) {
                continue;
            }
            while (true) {
                Map.Entry<Integer, Integer> ceil = map.ceilingEntry(f.p);
                if (ceil == null || ceil.getValue() > f.b) {
                    break;
                }
                map.remove(ceil.getKey());
            }
            map.put(f.p, f.b);
        }
        return ans;
    }
}

class Fountain {
    int b;
    int p;
}
