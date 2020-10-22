package on2020_10.on2020_10_20_Library_Checker.Line_Add_Get_Min0;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class LineAddGetMinTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(2));

    public Test create(int testNum) {
        int n = random.nextInt(1, 1000);
        long[][] line = new long[n][2];
        for (int i = 0; i < n; i++) {
            line[i][0] = random.nextInt(-(int) 1e9, (int) 1e9);
            line[i][1] = random.nextLong(-(long) 1e18, (long) 1e18);
        }
        int q = random.nextInt(1, 1000);
        long[] query = new long[q];
        for (int i = 0; i < q; i++) {
            query[i] = random.nextInt(-(int) 1e9, (int) 1e9);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        for (long[] l : line) {
            printLine(in, l[0], l[1]);
        }
        for (int i = 0; i < q; i++) {
            printLine(in, 1, query[i]);
        }
        return new Test(in.toString(), solve(line, query));
    }

    public String solve(long[][] lines, long[] query) {
        StringBuilder ans = new StringBuilder();
        for (long x : query) {
            long y = (long) 3e18;
            for (long[] line : lines) {
                y = Math.min(y, line[0] * x + line[1]);
            }
            printLine(ans, y);
        }
        return ans.toString();
    }
}
