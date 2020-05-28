package on2020_05.on2020_05_29_Codeforces___Playrix_Codescapes_Cup__Codeforces_Round__413__rated__Div__1___Div__2_.D__Field_expansion;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DFieldExpansion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int h = in.readInt();
        int w = in.readInt();
        int n = in.readInt();
        int[] mul = new int[n];
        in.populate(mul);
        Randomized.shuffle(mul);
        Arrays.sort(mul);
        SequenceUtils.reverse(mul);

        for (int i = 0; i <= mul.length; i++) {
            if (solve(w, h, a, b, Arrays.copyOf(mul, i))) {
                out.println(i);
                return;
            }
        }
        out.println(-1);
    }

    public boolean solve(int w, int h, int a, int b, int[] mul) {
        int n = mul.length;
        int twoCnt = 0;
        while (n >= 1 && mul[n - 1] == 2) {
            twoCnt++;
            n--;
        }

        mul = Arrays.copyOf(mul, n);
        long[] prod = new long[1 << n];

        prod[0] = 1;
        int limit = (int) 1e5;
        for (int i = 1; i < (1 << n); i++) {
            int lowest = Integer.lowestOneBit(i);
            if (lowest == i) {
                prod[i] = mul[Log2.floorLog(lowest)];
            } else {
                prod[i] = Math.min(limit, prod[i - lowest] * prod[lowest]);
            }
        }

        int mask = (1 << n) - 1;
        for (int i = 0; i < (1 << n); i++) {
            {
                //w and a
                long logReq = Log2.ceilLog(DigitUtils.ceilDiv(a, w * prod[i]))
                        + Log2.ceilLog(DigitUtils.ceilDiv(b, h * prod[mask - i]));
                if (logReq <= twoCnt) {
                    return true;
                }
            }
            {
                //w and a
                long logReq = Log2.ceilLog(DigitUtils.ceilDiv(b, w * prod[i]))
                        + Log2.ceilLog(DigitUtils.ceilDiv(a, h * prod[mask - i]));
                if (logReq <= twoCnt) {
                    return true;
                }
            }
        }
        return false;
    }
}
