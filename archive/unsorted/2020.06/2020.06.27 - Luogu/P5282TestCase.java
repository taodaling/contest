package contest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P5282TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 2);
        int p = 2147481811;//BigInteger.valueOf(1000000000).nextProbablePrime().intValue();
        return new Test("1\n" + n + " " + p, "" + solve(n, p));
    }

    public int solve(int n, int p) {
        long ans = 1;
        for (int i = 1; i <= n; i++) {
            ans *= i;
            ans %= p;
        }
        return (int) ans;
    }
}
