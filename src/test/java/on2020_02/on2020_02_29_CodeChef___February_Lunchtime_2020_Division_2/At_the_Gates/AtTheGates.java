package on2020_02.on2020_02_29_CodeChef___February_Lunchtime_2020_Division_2.At_the_Gates;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.BitSet;

public class AtTheGates {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        BitSet bit = new BitSet(n);
        for (int i = 0; i < n; i++) {
            if (in.readChar() == 'H') {
                bit.set(i, true);
            } else {
                bit.set(i, false);
            }
        }

        for (int i = 0; i < k; i++) {
            int index = n - i - 1;
            if (bit.get(index)) {
                bit.flip(0, index);
            }
            bit.set(index, false);
        }

        int count = bit.cardinality();
        out.println(count);
    }
}
