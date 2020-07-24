package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CMastermindTestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, n);
            b[i] = random.nextInt(0, n);
        }
        int x = CMastermind.getX(a, b);
        int y = CMastermind.getY(a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, x, y);
        for (int i = 0; i < n; i++) {
            in.append(b[i] + 1).append(' ');
        }
        printLine(in);
        for (int i = 0; i < n; i++) {
            in.append(a[i] + 1).append(' ');
        }

        return new Test(in.toString(), "YES");
    }
}
