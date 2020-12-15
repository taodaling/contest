package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.problem.PartitionNumber;

public class PartitionFunction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = 998244353;
        PartitionNumber pn = new PartitionNumber(n, mod, new IntPolyNTT(mod));
        for(int i = 0; i <= n; i++){
            out.append(pn.query(i)).append(' ');
        }

    }
}
