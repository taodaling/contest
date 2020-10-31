package on2020_10.on2020_10_23_AtCoder___AtCoder_Beginner_Contest_164.F___I_hate_Matrix_Construction;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FIHateMatrixConstructionTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int[][] mat = new int[n][n];

        int[] rowi = new int[n];
        int[] rowu = new int[n];
        int[] coli = new int[n];
        int[] colu = new int[n];
        Arrays.fill(rowi, -1);
        Arrays.fill(coli, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = random.nextInt(0, 1 << 10);
                rowi[i] &= mat[i][j];
                rowu[i] |= mat[i][j];
                coli[j] &= mat[i][j];
                colu[j] |= mat[i][j];
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);

        int[] s = new int[n];
        int[] t = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = random.nextInt(0, 1);
            t[i] = random.nextInt(0, 1);
        }
        printLine(in, s);
        printLine(in, t);
        for (int i = 0; i < n; i++) {
            in.append(s[i] == 0 ? rowi[i] : rowu[i]).append(' ');
        }
        printLine(in);
        for (int i = 0; i < n; i++) {
            in.append(t[i] == 0 ? coli[i] : colu[i]).append(' ');
        }
        printLine(in);
        return new Test(in.toString(), null);
    }
}
