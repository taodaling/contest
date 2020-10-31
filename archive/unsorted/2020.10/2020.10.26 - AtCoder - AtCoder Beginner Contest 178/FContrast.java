package contest;

import sun.security.acl.AclEntryImpl;
import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;

public class FContrast {
    Debug debug = new Debug(false);

    int id;

    public int[] nextKey(int x) {
        return new int[]{x, id++};
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] b = new int[n];
        in.populate(b);
        MultiSet<Integer> bMap = new MultiSet<>();
        TreeMap<int[], Integer> used = new TreeMap<>((x, y) -> x[0] == y[0] ? Integer.compare(x[1], y[1]) :
                Integer.compare(x[0], y[0]));
        for (int x : b) {
            bMap.add(x);
        }
        for (int i = 0; i < n; i++) {
            int j = i;
            while (j + 1 < n && a[j + 1] == a[j]) {
                j++;
            }
            int x = a[i];
            int c = j - i + 1;
            List<Integer> forThis = new ArrayList<>();
            while (c > forThis.size()) {
                Integer floor = bMap.floor(x - 1);
                if (floor == null) {
                    floor = bMap.ceil(x + 1);
                }
                if (floor == null) {
                    break;
                }
                bMap.remove(floor);
                forThis.add(floor);
            }
            while (c > forThis.size() && !used.isEmpty()) {
                Map.Entry<int[], Integer> floor = used.floorEntry(nextKey(x - 1));
                if (floor == null) {
                    floor = used.ceilingEntry(nextKey(x));
                }
                if (floor == null) {
                    break;
                }
                used.remove(floor.getKey());
                forThis.add(floor.getKey()[0]);
                used.put(nextKey(x), floor.getValue());
                bMap.remove(x);
            }
            if (c > forThis.size()) {
                out.println("No");
                return;
            }
            for (int y : forThis) {
                used.put(nextKey(y), x);
            }
            i = j;
        }
        assert bMap.size() == 0;
        Map<Integer, List<Integer>> reverse = new HashMap<>();
        for (Map.Entry<int[], Integer> entry : used.entrySet()) {
            reverse.computeIfAbsent(entry.getValue(), x -> new ArrayList<>()).add(entry.getKey()[0]);
        }

        debug.debug("used", used);
        debug.debug("reverse", reverse);
        out.println("Yes");
        for (int x : a) {
            int y = pop(reverse.get(x));
            assert y != x;
            out.append(y).append(' ');
        }
    }

    public Integer pop(List<Integer> list) {
        assert !list.isEmpty();
        return list.remove(list.size() - 1);
    }
}
