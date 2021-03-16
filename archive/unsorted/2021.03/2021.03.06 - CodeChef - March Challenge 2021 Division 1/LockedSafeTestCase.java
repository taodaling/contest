package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.SparseTable;
import template.primitve.generated.datastructure.LongSparseTable;
import template.rand.RandomWrapper;

public class LockedSafeTestCase {
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
        int n = random.nextInt(1, 1000000);
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextLong(0, (1L << 60) - 1);
        }
        //long ans = solve(a);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        printLine(in, a);
        return new Test(in.toString(), null);
    }

    public long solve(long[] a) {
        long ans = 0;
        LongSparseTable or = new LongSparseTable(i -> a[i], a.length, (x, y) -> x | y);
        LongSparseTable and = new LongSparseTable(i -> a[i], a.length, (x, y) -> x & y);
        LongSparseTable max = new LongSparseTable(i -> a[i], a.length, Math::max);
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                if (or.query(i, j) - and.query(i, j) >= max.query(i, j)) {
                    ans++;
                }
            }
        }
        return ans;
    }


}
