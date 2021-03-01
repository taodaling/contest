package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DZookeeperAndTheInfiniteZoo {
    boolean possible(int u, int v) {
        if (u == v) {
            return true;
        }
        if (u > v) {
            return false;
        }
        if (v == 0) {
            return true;
        }
        if (u == 0) {
            return false;
        }
        int hu = Integer.highestOneBit(u);
        int hv = Integer.highestOneBit(v);
        if (hu > hv) {
            return false;
        }
        if (hu == hv) {
            return possible(u - hu, v - hv);
        }
        u -= hu;
        v -= hv;
        while (u != 0 && v != 0) {
            if (Integer.lowestOneBit(u) > Integer.lowestOneBit(v)) {
                return false;
            }
            u -= Integer.lowestOneBit(u);
            v -= Integer.lowestOneBit(v);
        }
        return v == 0;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int u = in.ri();
        int v = in.ri();
        out.println(possible(u, v) ? "YES" : "NO");
    }
}
