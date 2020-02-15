package on2020_02.on2020_02_08_Codeforces_Round__578__Div__2_.C__Round_Corridor;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class CRoundCorridor {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        BigInteger n = BigInteger.valueOf(in.readLong());
        BigInteger m = BigInteger.valueOf(in.readLong());
        BigInteger lcm = n.multiply(m).divide(n.gcd(m));

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int sx = in.readInt();
            long sy = in.readLong() - 1;
            int ex = in.readInt();
            long ey = in.readLong() - 1;

            BigInteger s = BigInteger.valueOf(sy).multiply(sx == 1 ? m : n);
            BigInteger e = BigInteger.valueOf(ey).multiply(ex == 1 ? m : n);

            BigInteger cnt = s.divide(lcm).subtract(e.divide(lcm));
            if (cnt.equals(BigInteger.ZERO)) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }
    }
}
