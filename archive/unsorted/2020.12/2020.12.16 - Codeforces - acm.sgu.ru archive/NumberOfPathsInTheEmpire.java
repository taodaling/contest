package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

public class NumberOfPathsInTheEmpire {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        BigInteger[] prev = new BigInteger[2];
        BigInteger[] next = new BigInteger[2];
        Arrays.fill(prev, BigInteger.ZERO);
        prev[0] = BigInteger.valueOf(1);
        for (int i = 0; i < m; i++) {
            next[0] = prev[1];
            next[1] = BigInteger.valueOf(n).multiply(prev[0])
                    .add(prev[1].multiply(BigInteger.valueOf(2)));
            BigInteger[] tmp = prev;
            prev = next;
            next = tmp;
        }
        out.println(prev[0]);
    }
}
