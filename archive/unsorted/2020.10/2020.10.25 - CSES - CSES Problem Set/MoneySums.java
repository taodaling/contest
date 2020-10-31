package contest;

import template.datastructure.BitSet;
import template.io.FastInput;

import java.io.PrintWriter;

public class MoneySums {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        int limit = (int) 1e5;
        BitSet bs = new BitSet(limit + 1);
        bs.set(0);
        for (int y : x) {
            BitSet t = bs.clone();
            bs.rightShift(y);
            bs.or(t);
        }
        out.println(bs.size() - 1);
        for (int i = bs.nextSetBit(1); i < bs.capacity(); i = bs.nextSetBit(i + 1)) {
            out.println(i);
        }
    }
}
