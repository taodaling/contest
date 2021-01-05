package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EDuffAsAQueenTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        int n = random.nextInt(3, 3);
        int q = random.nextInt(3, 3);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(8);
        }
        int[][] qs = new int[q][];
        for (int i = 0; i < q; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (random.nextInt(2) == 0) {
                qs[i] = new int[]{1, l, r, random.nextInt(8)};
            } else {
                qs[i] = new int[]{2, l, r};
            }
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        printLine(in, a);
        for(int[] qu : qs){
            printLine(in, qu);
        }
        StringBuilder out = new StringBuilder();
        int[] ans = solve(a, qs);
        for(int x : ans){
            printLine(out, x);
        }
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] qs) {
        List<Integer> ans = new ArrayList<>();
        for (int[] q : qs) {
            int l = q[1] - 1;
            int r = q[2] - 1;

            if (q[0] == 1) {
                for(int j = l; j <= r; j++){
                    a[j] ^= q[3];
                }
            }else{
                template.datastructure.LinearBasis lb = new template.datastructure.LinearBasis();
                for(int j = l; j <= r; j++){
                    lb.add(a[j]);
                }
                ans.add((int)lb.xorNumberCount());
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
}
