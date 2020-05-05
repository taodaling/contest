package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class DSomethingWithXORQueriesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
        int[] val = new int[n];
        for (int i = 0; i < n; i++) {
            val[i] = i;
        }
        RandomWrapper.INSTANCE = random;
        Randomized.shuffle(val);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int x : val) {
            in.append(x).append(' ');
        }
        return new Test(in.toString(), null);
    }


}
