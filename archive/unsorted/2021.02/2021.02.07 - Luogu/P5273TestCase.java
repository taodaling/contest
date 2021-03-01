package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.polynomial.IntPolyNTT;
import template.rand.RandomWrapper;

public class P5273TestCase {
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
        int n = random.nextInt(1, 100);
        long k = random.nextLong(0, (long) 10000);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, 2);
        }
        int[] ans = solve(a, k);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < 100; i++) {
            in.append(0);
        }
        printLine(in, k);
        printLine(in, a);
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, long k) {
        int[] pow = new IntPolyNTT(998244353)
                .modpowBF(a, k, a.length);
        pow = Arrays.copyOf(pow, a.length);
        return pow;
    }


}
