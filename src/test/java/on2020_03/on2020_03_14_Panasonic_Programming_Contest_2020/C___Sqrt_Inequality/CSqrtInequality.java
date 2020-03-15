package on2020_03.on2020_03_14_Panasonic_Programming_Contest_2020.C___Sqrt_Inequality;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;

public class CSqrtInequality {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        BigInteger a = BigInteger.valueOf(in.readInt());
        BigInteger b = BigInteger.valueOf(in.readInt());
        BigInteger c = BigInteger.valueOf(in.readInt());


        BigInteger ans = c.subtract(a).subtract(b).pow(2).subtract(BigInteger.valueOf(4).multiply(a).multiply(b));
        out.println(c.subtract(a).subtract(b).signum() > 0 && ans.signum() > 0 ? "Yes" : "No");
    }
}
