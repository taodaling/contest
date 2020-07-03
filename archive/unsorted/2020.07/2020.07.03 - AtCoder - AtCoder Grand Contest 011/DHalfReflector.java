package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;


public class DHalfReflector {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        BitSet bs = new BitSet(n);
        for (int i = 0; i < n; i++) {
            bs.set(i, 'B' - in.readChar() == 1);
        }

        tmp = new BitSet(n);
        int now = 0;
        while (now < k && shift <= n) {
            now++;
            next(bs);
        }

        if (now == k) {
            output(bs, out);
            return;
        }

        k -= now;
        if(k % 2 == 1){
            next(bs);
        }

        output(bs, out);
    }

    public void output(BitSet bs, FastOutput out){
        for(int i = 0; i < bs.capacity(); i++){
            out.append(bs.get(i) ? 'A' : 'B');
        }
    }

    int shift = 0;
    BitSet tmp;

    public void next(BitSet bs) {
        if (bs.get(0)) {
            bs.clear(0);
            return;
        }
        shift++;
        tmp.copy(bs);
        tmp.leftShift(1);
        bs.fill(true);
        bs.xor(tmp);
        bs.set(bs.capacity() - 1);
    }

}
