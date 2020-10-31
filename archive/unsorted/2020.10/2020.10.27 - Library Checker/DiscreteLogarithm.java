package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GenericModLog;

public class DiscreteLogarithm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        int m = in.readInt();

        GenericModLog log = new GenericModLog(x, m);
        int k = log.log(y);
        out.println(k);
    }
}
