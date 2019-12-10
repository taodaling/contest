package on2019_12.on2019_12_10_.LUOGU4195;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GenericModLog;

public class LUOGU4195 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int p = in.readInt();
        int b = in.readInt();
        if (a == 0 && b == 0 && p == 0) {
            throw new UnknownError();
        }
        GenericModLog log = new GenericModLog(a, p);
        int x = log.log(b);
        if (x == -1) {
            out.println("No Solution");
        } else {
            out.println(x);
        }
    }
}
