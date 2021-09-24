package on2021_07.on2021_07_26_CodeChef___Practice_Medium_.GCD_Sums;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GCDSumsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        int n = random.nextInt(200000, 200000);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, (int) 1e9);
        }
        List<int[]> qs = new ArrayList<>();
        for (int i = 0; i < 1e6; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            qs.add(new int[]{l, r});
        }
//        for (int i = 0; i < n; i++) {
//            for (int j = i; j < n; j++) {
//                qs.add(new int[]{i + 1, j + 1});
//            }
//        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, qs.size());
        printLine(in, a);
        for (int[] q : qs) {
            printLine(in, q);
        }
        return new Test(in.toString());
    }

    int mod = (int) 1e9 + 7;

//    public String solve(int[] a, List<int[]> qs) {
//        StringBuilder ans = new StringBuilder();
//        for (int[] q : qs) {
//            int l = q[0] - 1;
//            int r = q[1] - 1;
//            long sum = 0;
//            for (int i = l; i <= r; i++) {
//                for (int j = i; j <= r; j++) {
//                    int g = 0;
//                    for (int k = i; k <= j; k++) {
//                        g = GCDs.gcd(g, a[k]);
//                    }
//                    sum += g;
//                }
//            }
//            sum %= mod;
//            ans.append(sum).append('\n');
//        }
//        return ans.toString();
//    }
}
