package on2019_11.on2019_11_07_AtCoder_Grand_Contest_015.D___A_or___or_B_Problem;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntervalBitwiseOrExpandGroup;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();

        out.println(IntervalBitwiseOrExpandGroup.expand(a, b));
    }
}
