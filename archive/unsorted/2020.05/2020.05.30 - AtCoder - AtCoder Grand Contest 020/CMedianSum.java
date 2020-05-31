package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.BitSet;


public class CMedianSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += a[i];
        }
        BitSet bs = new BitSet(sum + 1);
        BitSet tmp = new BitSet(sum + 1);
        bs.set(0);

        for (int x : a) {
            tmp.copy(bs);
            tmp.rightShift(x);
            bs.or(tmp);
        }

        int ans = DigitUtils.ceilDiv(sum, 2);
        while (!bs.get(ans)) {
            ans++;
        }
        out.println(ans);
    }
}
