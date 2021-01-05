package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EInversionSwapSortTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 50);
        int[] data = new int[n];
        for(int i = 0; i < n; i++){
            data[i] = random.nextInt(1, 30);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int i = 0; i < n; i++){
            in.append(data[i]).append(' ');
        }
        return new Test(in.toString(), null);
    }
}
