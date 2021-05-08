package on2021_05.on2021_05_01_Google_Coding_Competitions___Round_1C_2021___Code_Jam_2021.Roaring_Years;



import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;

public class RoaringYears {
    String s;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long Y = in.rl();
        s = "" + Y;
        int len = s.length();
        best = null;

        for (int prefix = 1; prefix <= len; prefix++) {
            for (int i = 0; i <= 1; i++) {
                long pv = Long.parseLong(s.substring(0, prefix)) + i;
                compute(BigInteger.valueOf(pv));
            }
        }

        for (int i = 1; i <= 20; i++) {
            BigInteger cand = BigInteger.valueOf(10).pow(i);
            for (int j = 0; j < 20 && cand.compareTo(BigInteger.ZERO) > 0; j++, cand = cand.subtract(BigInteger.ONE)) {
                compute(cand);
            }
        }

        out.printf("Case #%d: %s\n", testNumber, best);
    }

    StringBuilder builder = new StringBuilder();
    String best = null;

    public void compute(BigInteger start) {
        BigInteger pv = start;
        builder.setLength(0);
        builder.append(pv);
        pv = pv.add(BigInteger.ONE);
        builder.append(pv);
        while (compare(builder, s) <= 0) {
            pv = pv.add(BigInteger.ONE);
            builder.append(pv);
        }

        if (best == null || compare(best, builder) > 0) {
            best = builder.toString();
        }
    }

    public int compare(CharSequence a, CharSequence b) {
        int res = Integer.compare(a.length(), b.length());
        if (res == 0) {
            for (int i = 0; res == 0 && i < a.length(); i++) {
                res = Integer.compare(a.charAt(i), b.charAt(i));
            }
        }
        return res;
    }
}
