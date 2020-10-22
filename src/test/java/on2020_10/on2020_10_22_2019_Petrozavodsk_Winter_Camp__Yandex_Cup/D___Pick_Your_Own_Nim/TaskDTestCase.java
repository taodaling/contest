package on2020_10.on2020_10_22_2019_Petrozavodsk_Winter_Camp__Yandex_Cup.D___Pick_Your_Own_Nim;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.LinearBasis;
import template.rand.RandomWrapper;

public class TaskDTestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int limit = (1 << 20) - 1;
        int n = random.nextInt(1, 5);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, limit);
        }
        List<int[]> b = new ArrayList<>();
        int m = random.nextInt(1, 5);
        for (int i = 0; i < m; i++) {
            int size = random.nextInt(1, 5);
            int[] x = new int[size];
            for (int j = 0; j < size; j++) {
                x[j] = random.nextInt(0, limit);
            }
            b.add(x);
        }

        boolean ans = solve(a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, m);
        for(int[] x : b){
            in.append(x.length).append(' ');
            printLine(in, x);
        }
        return new Test(in.toString(), ans ? "1" : "-1");
    }

    public boolean bf(List<int[]> b, int i, LinearBasis basis) {
        if (i >= b.size()) {
            return true;
        }
        for (int x : b.get(i)) {
            if (basis.contain(x)) {
                continue;
            }
            LinearBasis clone = basis.clone();
            clone.add(x);
            if (bf(b, i + 1, clone)) {
                return true;
            }
        }
        return false;
    }

    public boolean solve(int[] a, List<int[]> b) {
        LinearBasis lb = new LinearBasis();
        for (int x : a) {
            if (lb.add(x) == -1) {
                return false;
            }
        }
        return bf(b, 0, lb);
    }
}
