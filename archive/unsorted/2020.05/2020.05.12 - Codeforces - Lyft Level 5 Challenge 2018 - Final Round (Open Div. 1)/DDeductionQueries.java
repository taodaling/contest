package contest;

import template.datastructure.XorDeltaDSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;

public class DDeductionQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();

        map.put(-1, 0);
        XorDeltaDSU dsu = new XorDeltaDSU(q * 2 + 100);
        int last = 0;
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int l = in.readInt() ^ last;
            int r = in.readInt() ^ last;
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            l = id(l - 1);
            r = id(r);
            if (t == 1) {
                int x = in.readInt() ^ last;
                if (dsu.find(l) == dsu.find(r)) {
                    continue;
                }
                dsu.merge(l, r, x);
            } else {
                if (dsu.find(l) != dsu.find(r)) {
                    out.println(-1);
                    last = 1;
                    continue;
                }
                last = dsu.delta(l, r);
                out.println(last);
            }
        }
    }

    IntegerHashMap map = new IntegerHashMap((int) 5e5, false);

    public int id(int i) {
        int id = map.getOrDefault(i, -1);
        if (id == -1) {
            id = map.size();
            map.put(i, id);
        }
        return id;
    }
}
