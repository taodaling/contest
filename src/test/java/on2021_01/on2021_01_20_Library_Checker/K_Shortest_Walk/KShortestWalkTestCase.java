package on2021_01.on2021_01_20_Library_Checker.K_Shortest_Walk;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class KShortestWalkTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(1);

    public Test create(int testNum) {
        int n = random.nextInt(2, 5);
        int m = random.nextInt(n, 10);
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = random.nextInt(0, n - 1);
            edges[i][1] = random.nextInt(0, n - 1);
            edges[i][2] = random.nextInt(0, 10);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m, 0, n - 1, 10);
        for (int[] e : edges) {
            printLine(in, e);
        }

        String ans = new ExternalExecutor("F:\\geany\\main.exe")
                .invoke(in.toString());
        return new Test(in.toString(), ans);
    }

}
