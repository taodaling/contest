package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class FSouvenirsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }


    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = (int) 1e5;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        int m = (int) 3e5;
        int[][] qs = new int[m][2];
        for (int i = 0; i < m; i++) {
            qs[i][0] = random.nextInt(1, n);
            qs[i][1] = random.nextInt(1, n);
            if (qs[i][0] > qs[i][1]) {
                SequenceUtils.swap(qs[i], 0, 1);
            }
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, m);
        for (int i = 0; i < m; i++) {
            printLine(in, qs[i]);
        }
        return new Test(in.toString(), null);
    }
}
