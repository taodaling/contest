package contest;

import template.datastructure.BitSet;
import template.datastructure.GenericLinearBasis;
import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;
import java.util.Arrays;

public class DMishaAndXOR {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int size = 2000;
        GenericLinearBasis lb = new GenericLinearBasis(size);
        int[] indices = new int[size];
        Arrays.fill(indices, -1);
        BitSet x = new BitSet(size);
        BitSet res = new BitSet(size);
        for (int i = 0; i < m; i++) {
            BigInteger bits = new BigInteger(in.rs());
            x.fill(false);
            for (int j = 0; j < size; j++) {
                if (bits.testBit(j)) {
                    x.set(j);
                }
            }
            if (!lb.representationOriginal(x, res)) {
                out.println(0);
            } else {
                out.append(res.size()).append(' ');
                for (int index = res.nextSetBit(0); index < res.capacity(); index = res.nextSetBit(index + 1)) {
                    out.append(indices[index]).append(' ');
                }
                out.println();
            }
            int index = lb.add(x);
            if (index >= 0) {
                indices[index] = i;
            }
        }
    }
}
