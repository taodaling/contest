package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class RiolkusMockCCCS5KeenKeenerMultisetTestCase {
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

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int q = random.nextInt(1, 10);
        int[] a = new int[n];

        int limit = (int) 3;
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, limit);
        }
        List<int[]> qs = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int t = random.nextInt(1, 3);
            int l = random.nextInt(0, limit);
            int r = random.nextInt(0, limit);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (t == 1) {
                qs.add(new int[]{t, random.nextInt(0, limit)});
            } else if (t == 2) {
                qs.add(new int[]{t, l, r, random.nextInt(0, limit)});
            } else {
                qs.add(new int[]{t, l, r});
            }
        }
        StringBuilder in = new StringBuilder();
        int[] ans = solve(a, qs.toArray(new int[0][]));
        StringBuilder out = new StringBuilder();
        printLine(in, n, q);
        printLine(in, a);
        for(int[] x : qs){
            printLine(in, x);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] qs) {
        IntegerArrayList ans = new IntegerArrayList();
        a = a.clone();
        for (int[] q : qs) {
            if (q[0] == 1) {
                a = Arrays.copyOf(a, a.length + 1);
                a[a.length - 1] = q[1];
            } else if (q[0] == 2) {
                int l = q[1];
                int r = q[2];
                int x = q[3];
                for (int i = 0; i < a.length; i++) {
                    if (a[i] >= l && a[i] <= r) {
                        a[i] ^= x;
                    }
                }
            } else if (q[0] == 3) {
                int l = q[1];
                int r = q[2];
                int sum = 0;
                for (int i = 0; i < a.length; i++) {
                    if (a[i] >= l && a[i] <= r) {
                        sum ^= a[i];
                    }
                }
                ans.add(sum);
            }
        }
        return ans.toArray();
    }
}
