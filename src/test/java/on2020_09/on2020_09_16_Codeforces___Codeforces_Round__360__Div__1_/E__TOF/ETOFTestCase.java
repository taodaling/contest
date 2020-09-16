package on2020_09.on2020_09_16_Codeforces___Codeforces_Round__360__Div__1_.E__TOF;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class ETOFTestCase {
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
        int n = 5000;
        int m = 5000;
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int i = 1; i <= n; i++) {
            printLine(in, i, (i + 1) % n + 1);
        }
        return new Test(in.toString(), null);
    }
}
