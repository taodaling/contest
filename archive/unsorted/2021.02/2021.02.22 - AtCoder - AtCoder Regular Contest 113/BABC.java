package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntegerModPowerLink;

public class BABC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.ri();
        int B = in.ri();
        int C = in.ri();
        IntegerModPowerLink link = new IntegerModPowerLink(new int[]{A, B, C});
        int ans = link.query(0, 2, 10);
        out.println(ans);
    }
}
