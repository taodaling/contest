package on2021_06.on2021_06_19_Library_Checker.Characteristic_Polynomial;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CharacteristicPolynomialTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    int mod = 998244353;

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = random.nextInt(0, 3);
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int[] row : mat) {
            printLine(in, row);
        }
        String res = new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString());
        return new Test(in.toString(), res);
    }
}
