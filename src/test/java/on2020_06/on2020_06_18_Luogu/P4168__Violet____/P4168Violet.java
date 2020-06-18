package on2020_06.on2020_06_18_Luogu.P4168__Violet____;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.problem.MaxOccurenceNumberInInterval;
import template.utils.Debug;

public class P4168Violet {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        MaxOccurenceNumberInInterval handler = new MaxOccurenceNumberInInterval(a);

        int x = 0;
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int r = in.readInt();
            l = DigitUtils.mod(l + x - 1, n);
            r = DigitUtils.mod(r + x - 1, n);
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            x = handler.query(l, r);
            out.println(x);
            debug.debug("x", x);
        }
    }
}
