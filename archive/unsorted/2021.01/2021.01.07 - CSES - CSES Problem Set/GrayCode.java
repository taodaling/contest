package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class GrayCode {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        for(int i = 0; i < 1 << n; i++){
            long x = template.math.GrayCode.apply(i);
            for(int j = n - 1; j >= 0; j--){
                out.append(Bits.get(x, j));
            }
            out.println();
        }
    }
}
