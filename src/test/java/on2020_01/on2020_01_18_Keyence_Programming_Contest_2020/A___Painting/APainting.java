package on2020_01.on2020_01_18_Keyence_Programming_Contest_2020.A___Painting;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class APainting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int n = in.readInt();
        out.println(DigitUtils.ceilDiv(n, Math.max(h, w)));
    }
}
