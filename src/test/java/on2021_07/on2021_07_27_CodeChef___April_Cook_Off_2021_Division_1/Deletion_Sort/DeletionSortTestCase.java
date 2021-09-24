package on2021_07.on2021_07_27_CodeChef___April_Cook_Off_2021_Division_1.Deletion_Sort;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class DeletionSortTestCase {
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
        int t = random.nextInt(1, 3);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, t);
        for (int tc = 0; tc < t; tc++) {
            int n = random.nextInt(1, 10);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = random.nextInt(1, 10);
            }
            int ans = solve(a);
            printLine(in, n);
            printLine(in, a);
            printLine(out, ans);
        }
        return new Test(in.toString(), out.toString());
    }

    public int solve(int[] a) {
        int ans = 0;
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Set<Integer> set = new HashSet<>();
                for (int k = i; k <= j; k++) {
                    set.add(a[k]);
                }
                boolean ok = true;
                int last = -1;
                for (int x : a) {
                    if (set.contains(x)) {
                        continue;
                    }
                    if (x < last) {
                        ok = false;
                        break;
                    }
                    last = x;
                }
                if (ok) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
