package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.D__Take_a_Guess;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class DTakeAGuess {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        this.in = in;
        this.out = out;
        int[] and = new int[n];
        int[] or = new int[n];
        int[] xor = new int[n];
        for (int i = 1; i < n; i++) {
            and[i] = and(0, i);
            or[i] = or(0, i);
            xor[i] = and[i] ^ or[i];
        }
        debug.debug("xor", xor);
        int a12 = and(1, 2);
        int o12 = or(1, 2);
        int x12 = a12 ^ o12;
        int x0 = 0;
        int one = 0;
        int zero = (1 << 30) - 1;
        for (int j = 1; j < n; j++) {
            one |= and[j];
            zero &= or[j];
        }
        for (int j = 0; j < 30; j++) {
            if (Bits.get(one, j) == 1) {
                x0 |= 1 << j;
            } else if (Bits.get(zero, j) == 0) {
            } else {
                if (Bits.get(a12, j) == 0) {
                    x0 |= 1 << j;
                }
            }
        }
        int[] val = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = x0 ^ xor[i];
        }
        debug.debug("val", val);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(val[i], val[j]), 0, n);
        int kth = val[indices[k - 1]];
        out.append("finish ").append(kth).println().flush();

    }

    Debug debug = new Debug(false);
    FastInput in;
    FastOutput out;

    public int and(int a, int b) {
        out.append("and ").append(a + 1).append(' ').append(b + 1).println().flush();
        return in.ri();
    }

    public int or(int a, int b) {
        out.append("or ").append(a + 1).append(' ').append(b + 1).println().flush();
        return in.ri();
    }
}
