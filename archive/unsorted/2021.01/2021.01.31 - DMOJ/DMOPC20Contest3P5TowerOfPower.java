package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntegerModPowerLink;

public class DMOPC20Contest3P5TowerOfPower {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[n];
        in.populate(a);
        IntegerModPowerLink link = new IntegerModPowerLink(i -> a[i]);
        out.println(link.query(0, n - 1, m));
    }
}
