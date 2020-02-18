package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ExtCRT;

public class P4777EXCRT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        ExtCRT crt = new ExtCRT();
        for(int i = 0; i < n; i++){
            long a = in.readLong();
            long b = in.readLong();
            crt.add(b, a);
        }
        out.println(crt.getValue());
    }
}
