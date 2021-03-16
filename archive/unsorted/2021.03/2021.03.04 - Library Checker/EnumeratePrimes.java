package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class EnumeratePrimes {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        BitSet bs = new BitSet(n + 1);
        bs.fill(false);
        bs.set(0, Math.min(n, 1));
        for (int i = 2; i * i <= n; i++) {
            if (bs.get(i)) {
                continue;
            }
            for (int j = i * i; j <= n; j += i) {
                bs.set(j);
            }
        }
//        bs.flip(0, n);
        out.append(bs.capacity() - bs.size()).append(' ');
        IntegerArrayList ans = new IntegerArrayList(n / 20);
        int index = -1;
        for (int i = bs.nextClearBit(0); i < bs.capacity(); i = bs.nextClearBit(i + 1)) {
            index++;
            if (index >= b && (index - b) % a == 0) {
                ans.add(i);
            }
        }
        debug.debug("bs", bs);
        out.append(ans.size()).println();
        for (int x : ans.toArray()) {
            out.append(x).append(' ');
        }
    }
}
