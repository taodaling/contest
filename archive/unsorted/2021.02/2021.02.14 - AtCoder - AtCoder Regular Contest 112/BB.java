package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongIntervalMap;

public class BB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long b = in.rl();
        long c = in.rl();
        LongIntervalMap map = new LongIntervalMap();
        //1111
        map.add(b - c / 2, b + 1);
        //11112
        map.add(-b, -b + (c - 1) / 2 + 1);
        //21112
        map.add(b, -(-b - (c - 2) / 2) + 1);
        //21111
        map.add(-b - (c - 1) / 2, -b + 1);
        out.println(map.total());
    }
}
