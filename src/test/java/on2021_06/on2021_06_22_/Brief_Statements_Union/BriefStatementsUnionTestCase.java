package on2021_06.on2021_06_22_.Brief_Statements_Union;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class BriefStatementsUnionTestCase {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        int[][] qs = new int[m][];
        for (int i = 0; i < m; i++) {
            int l = random.nextInt(0, n - 1);
            int r = random.nextInt(0, n - 1);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int v = random.nextInt(0, 1);
            qs[i] = new int[]{l + 1, r + 1, v};
        }

        String ans = solve(qs, n);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int[] q : qs) {
            printLine(in, q);
        }
        return new Test(in.toString(), ans);
    }

    public String solve(int[][] qs, int n) {
        int[] res = new int[qs.length];
        for (int i = 0; i < 1 << n; i++) {
            boolean[] ok = new boolean[qs.length];
            for (int j = 0; j < qs.length; j++) {
                int l = qs[j][0] - 1;
                int r = qs[j][1] - 1;
                int v = qs[j][2];
                int and = 1;
                for (int t = l; t <= r; t++) {
                    and &= Bits.get(i, t);
                }
                ok[j] = and == v;
            }
            for (int j = 0; j < qs.length; j++) {
                boolean valid = true;
                for (int k = 0; k < qs.length; k++) {
                    if (j == k) {
                        continue;
                    }
                    valid = valid && ok[k];
                }
                if (valid) {
                    res[j] = 1;
                }
            }
        }
        StringBuilder ans = new StringBuilder();
        for (int x : res) {
            ans.append(x);
        }
        return ans.toString();
    }
}
