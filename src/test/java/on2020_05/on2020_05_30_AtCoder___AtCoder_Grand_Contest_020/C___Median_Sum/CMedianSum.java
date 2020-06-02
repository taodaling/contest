package on2020_05.on2020_05_30_AtCoder___AtCoder_Grand_Contest_020.C___Median_Sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.datastructure.BitSet;


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
