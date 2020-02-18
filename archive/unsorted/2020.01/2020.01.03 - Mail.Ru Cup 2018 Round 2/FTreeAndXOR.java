package contest;

import org.omg.CORBA.INTERNAL;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.primitve.generated.IntegerMultiWayStack;
import template.problem.KthXorTwoElement;
import template.rand.Randomized;
import template.utils.Buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FTreeAndXOR {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();

        long[] weights = new long[n];
        for (int i = 1; i < n; i++) {
            int p = in.readInt() - 1;
            long w = in.readLong();
            weights[i] = weights[p] ^ w;
        }
        long mask = KthXorTwoElement.solve(weights, k);
        out.println(mask);
    }
}
