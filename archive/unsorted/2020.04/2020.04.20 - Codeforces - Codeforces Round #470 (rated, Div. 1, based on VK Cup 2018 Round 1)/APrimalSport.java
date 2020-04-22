package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class APrimalSport {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x2 = in.readInt();
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime(x2);
        int x0 = x2;
        for (int i = 3; i <= x2; i++) {
            boolean valid = false;
            for (IntegerIterator iterator = stack.iterator(x2); iterator.hasNext(); ) {
                int p = iterator.next();
                if (p >= i) {
                    continue;
                }
                int minMultiple = p * DigitUtils.ceilDiv(i, p);
                if (minMultiple != x2) {
                    continue;
                }
                valid = true;
                break;
            }

            if (!valid) {
                continue;
            }
            for (IntegerIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                int p = iterator.next();
                if (p >= i) {
                    continue;
                }
                x0 = Math.min(x0, (i / p - 1) * p + 1);
            }
        }

        out.println(x0);
    }
}
