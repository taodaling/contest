package on2020_05.on2020_05_11_Codeforces___AIM_Tech_Round_4__Div__1_.D__Dynamic_Shortest_Path;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DDynamicShortestPathTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
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
        int n = 100000;
        int m = 100000;
        int q = 2000;

        StringBuilder in = new StringBuilder();
        printLine(in, n, m, q);
        for (int i = 0; i < m / 2; i++) {
            int a = random.nextInt(1, 1);
            int b = random.nextInt(i + 2, i + 2);
            int c = random.nextInt(1, (int) 1e9);
            printLine(in, a, b, c);
        }

        for (int i = m / 2; i < m; i++) {
            int a = random.nextInt(1, n);
            int b = random.nextInt(1, n);
            int c = random.nextInt(1, (int) 1e9);
            printLine(in, a, b, c);
        }

        for (int i = 0; i < q; i++) {
            int c = 500;
            in.append(2).append(' ').append(c).append(' ');
            for (int j = 0; j < c; j++) {
                in.append(random.nextInt(1, m)).append(' ');
            }
            printLine(in);
        }
        return new Test(in.toString(), null);
    }
}
