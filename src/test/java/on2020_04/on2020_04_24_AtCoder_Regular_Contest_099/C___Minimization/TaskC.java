package on2020_04.on2020_04_24_AtCoder_Regular_Contest_099.C___Minimization;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        out.println(DigitUtils.ceilDiv(n - 1, k - 1));
    }
}
