package on2019_11.on2019_11_24_AtCoder_Beginner_Contest_146.C___Buy_an_Integer;



import com.fasterxml.jackson.databind.node.BigIntegerNode;
import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        BigInteger a = new BigInteger(in.readString());
        BigInteger b = new BigInteger(in.readString());
        BigInteger x = new BigInteger(in.readString());
        long l = 0;
        long r = (int)1e9;
        while (l < r) {
            long m = (l + r + 1) >>> 1;
            BigInteger mbi = BigInteger.valueOf(m);
            if (a.multiply(mbi).add(b.multiply(BigInteger.valueOf(mbi.toString(10).length())))
                    .compareTo(x) <= 0) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        out.println(l);
    }
}
