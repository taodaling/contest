package on2019_12.on2019_12_07_.IntExtCRTTest;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntExtCRT;

public class IntExtCRTTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntExtCRT crt = new IntExtCRT();
        for (int i = 0; i < n; i++) {
            if (!crt.add(in.readInt(), in.readInt())) {
                out.append("-1");
                return;
            }
        }
        out.append(crt.getValue());
    }
}
