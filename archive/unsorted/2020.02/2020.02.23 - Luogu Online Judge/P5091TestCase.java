package contest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P5091TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int a = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        String b = random.nextString('0', '9', random.nextInt(1, 2));

        BigInteger ans = BigInteger.valueOf(a).modPow(new BigInteger(b), BigInteger.valueOf(m));
        StringBuilder in = new StringBuilder();
        printLine(in, a, m, b);
        return new Test(in.toString(), ans.toString());
    }


}
