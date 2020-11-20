package contest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.CompareUtils;

public class P4314CPUTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        File dir = new File("/home/dalt/下载/3064");
        return new Test(FileUtils.readFile(dir, testNum + ".in"),
                FileUtils.readFile(dir, testNum + ".out"));

//        int n = random.nextInt(1, 5);
//        int m = random.nextInt(1, 5);
//        int[] a = new int[n];
//        int[][] op = new int[m][];
//        for (int i = 0; i < n; i++) {
//            a[i] = random.nextInt(-5, 5);
//        }
//        for (int i = 0; i < m; i++) {
//            char c = random.range('Q', 'A', 'P', 'C');
//            int l = random.nextInt(1, n);
//            int r = random.nextInt(1, n);
//            if (l > r) {
//                int tmp = l;
//                l = r;
//                r = tmp;
//            }
//            if (c == 'Q' || c == 'A') {
//                op[i] = new int[]{c, l, r};
//            } else {
//                int z = random.nextInt(-5, 5);
//                op[i] = new int[]{c, l, r, z};
//            }
//        }
//
//        int[] ans = solve(a, op);
//        StringBuilder in = new StringBuilder();
//        printLine(in, n);
//        printLine(in, a);
//        printLine(in, m);
//        for(int[] x : op){
//            in.append((char)x[0]);
//            for(int i = 1; i < x.length; i++){
//                in.append(' ').append(x[i]);
//            }
//            printLine(in);
//        }
//        StringBuilder out = new StringBuilder();
//        printLine(out, ans);
//        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[][] op) {
        List<Integer> collect = new ArrayList<>();
        a = a.clone();
        int[] b = a.clone();
        for (int[] x : op) {
            int c = x[0];
            int l = x[1] - 1;
            int r = x[2] - 1;
            if (c == 'Q') {
                collect.add(CompareUtils.maxOf(a, l, r));
            } else if (c == 'A') {
                collect.add(CompareUtils.maxOf(b, l, r));
            } else if (c == 'P') {
                int z = x[3];
                for (int i = l; i <= r; i++) {
                    a[i] += z;
                }
            }else{
                int z = x[3];
                for (int i = l; i <= r; i++) {
                    a[i] = z;
                }
            }
            for(int i = 0; i < a.length; i++){
                b[i] = Math.max(b[i], a[i]);
            }
        }

        return collect.stream().mapToInt(Integer::intValue).toArray();
    }
}
