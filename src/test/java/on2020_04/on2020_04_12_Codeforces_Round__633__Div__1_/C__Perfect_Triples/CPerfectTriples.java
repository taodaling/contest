package on2020_04.on2020_04_12_Codeforces_Round__633__Div__1_.C__Perfect_Triples;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CPerfectTriples {
    Debug debug = new Debug(true);

    int[] perm = new int[]{0, 2, 3, 1};

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        if (n <= 3) {
            out.println(n);
            return;
        }
        n -= 3;
        int h = 2;
        while (n > 3 * (1L << h)) {
            n -= 3 * (1L << h);
            h += 2;
        }

        long low = (n - 1) / 3;
        long a0 = Bits.setBit(low, h, true);
        long a1 = 0;
        for (int i = 0; i <= 60; i += 2) {
            int val = (int) ((a0 >> i) & 3);
            a1 |= (long)perm[val] << i;
        }

        if((n - 1) % 3 == 0){
            out.println(a0);
        }else if((n - 1) % 3 == 1){
            out.println(a1);
        }else{
            out.println(a1 ^ a0);
        }

    }


}
