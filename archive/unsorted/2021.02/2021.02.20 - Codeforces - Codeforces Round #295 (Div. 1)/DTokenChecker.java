package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

import java.math.BigInteger;
import java.util.Objects;

public class DTokenChecker extends AbstractChecker {
    public DTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int k = stdin.ri();
        int n = stdin.ri();
        int m = stdin.ri();
        int[] init = stdin.ri(k);
        int[][] op = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                op[i][j] = stdin.ri();
            }
            op[i][1]--;
        }
        BigInteger expAns = calc(init, op, expect, m);
        BigInteger actAns = calc(init, op, actual, m);
        return Objects.equals(expAns, actAns) ? Verdict.OK : Verdict.WA;
    }

    public BigInteger calc(int[] init, int[][] op, FastInput in, int m) {
        int t = in.ri();
        if (t > m || t < 0) {
            return null;
        }
        BigInteger[] vals = new BigInteger[init.length];
        for (int i = 0; i < init.length; i++) {
            vals[i] = BigInteger.valueOf(init[i]);
        }
        boolean[] used = new boolean[op.length];
        for (int i = 0; i < t; i++) {
            int index = in.ri() - 1;
            if (index < 0 || index >= op.length) {
                return null;
            }
            int item = op[index][1];
            if (used[index]) {
                return null;
            }
            used[index] = true;
            if (op[index][0] == 1) {
                vals[item] = BigInteger.valueOf(op[index][2]);
            } else if (op[index][0] == 2) {
                vals[item] = BigInteger.valueOf(op[index][2]).add(vals[item]);
            } else {
                vals[item] = BigInteger.valueOf(op[index][2]).multiply(vals[item]);
            }
        }
        BigInteger ans = BigInteger.ONE;
        for (BigInteger x : vals) {
            ans = ans.multiply(x);
        }
        return ans;
    }


}