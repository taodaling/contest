package on2020_09.on2020_09_11_Codeforces___Codeforces_Round__362__Div__1_.C__PLEASE;



import java.math.BigInteger;
import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.BigFraction;
import template.rand.RandomWrapper;

public class CPLEASETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(100000, 100000);
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = (long)1e18;
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(long x : a){
            in.append(x).append(' ');
        }
        return new Test(in.toString(), null);
    }


    public String solve(int[] a) {
        BigFraction[] state = new BigFraction[3];
        state[0] = state[2] = BigFraction.ZERO;
        state[1] = BigFraction.ONE;
        int prod = 1;
        for (int x : a) {
            prod *= x;
        }
        BigFraction half = new BigFraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        for (int i = 0; i < prod; i++) {
            BigFraction[] next = new BigFraction[3];
            next[0] = BigFraction.mul(half, BigFraction.plus(state[0], state[1]));
            next[1] = BigFraction.mul(half, BigFraction.plus(state[0], state[2]));
            next[2] = BigFraction.mul(half, BigFraction.plus(state[1], state[2]));
            state = next;
        }
        BigInteger mod = BigInteger.valueOf((int)1e9 + 7);
        return state[1].top().mod(mod) + "/" +
                state[1].bot().mod(mod);
    }
}
