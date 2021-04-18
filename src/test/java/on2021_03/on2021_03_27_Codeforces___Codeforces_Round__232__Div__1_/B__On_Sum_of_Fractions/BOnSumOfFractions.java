package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__232__Div__1_.B__On_Sum_of_Fractions;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigFraction;
import template.math.MillerRabin;

import java.math.BigInteger;

public class BOnSumOfFractions {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n == 2) {
            out.println("1/6");
            return;
        }
        int p1 = prev(n);
        int next = next(n + 1);
        //2..p1
        BigFraction ans = BigFraction.minus(new BigFraction(BigInteger.ONE, BigInteger.valueOf(2)),
                new BigFraction(BigInteger.ONE, BigInteger.valueOf(p1)));
        ans = BigFraction.plus(ans, new BigFraction(BigInteger.valueOf(n - p1 + 1), BigInteger.valueOf((long) p1 * next)));
        out.append(ans.top()).append('/').append(ans.bot()).println();
    }

    public int prev(int x) {
        while (!MillerRabin.mr(x, 10)) {
            x--;
        }
        return x;
    }

    public int next(int x) {
        while (!MillerRabin.mr(x, 10)) {
            x++;
        }
        return x;
    }
}
