package on2021_01.on2021_01_22_Luogu.P2741__USACO4_4______Frame_Up;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class P2741USACO44FrameUpTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(1);

    public Test create(int testNum) {
        int n = random.nextInt(3, 15);
        int m = random.nextInt(3, 15);
        char[][] mat = new char[n][m];
        SequenceUtils.deepFill(mat, '.');
        for (int i = 'A'; i <= 'Z'; i++) {
            int l = random.nextInt(0, m - 1);
            int r = random.nextInt(0, m - 1);
            int b = random.nextInt(0, n - 1);
            int t = random.nextInt(0, n - 1);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (b > t) {
                int tmp = b;
                b = t;
                t = tmp;
            }
            if (r - l <= 1 || t - b <= 1) {
                continue;
            }
            paint(mat, l, r, b, t, (char) i);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int i = 0; i < n; i++) {
            printLineObj(in, new String(mat[i]));
        }
        return new Test(in.toString(), null);
    }

    public void paint(char[][] mat, int l, int r, int b, int t, char c) {
        for (int i = b; i <= t; i++) {
            mat[i][l] = mat[i][r] = c;
        }
        for (int i = l; i <= r; i++) {
            mat[b][i] = mat[t][i] = c;
        }
    }
}
