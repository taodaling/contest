package contest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class HHintsOfSd0061TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 5);
        long A = random.nextLong(0, mask);
        long B = random.nextLong(0, mask);
        long C = random.nextLong(0, mask);
        int[] qs = new int[m];
        for (int i = 0; i < m; i++) {
            qs[i] = random.nextInt(0, n - 1);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m, A, B, C);
        for (int q : qs) {
            in.append(q).append(' ');
        }
        printLine(in);

        String ans = solve(n, m, A, B, C, qs);
        return new Test(in.toString(), ans);
    }

    public String solve(int n, int m, long A, long B, long C, int[] qs) {
        StringBuilder ans = new StringBuilder();
        ans.append("Case #1: ");
        x = A;
        y = B;
        z = C;
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = rng61() & mask;
        }
        Arrays.sort(data);
        for (int q : qs) {
            ans.append(data[q]).append(' ');
        }
        return ans.toString();
    }

    long mask = ((1L << 32) - 1);
    long x, y, z;

    long rng61() {
        long t;
        x = x ^ ((x << 16) & mask);
        x = x ^ (x >> 5);
        x = x ^ ((x << 1) & mask);
        t = x;
        x = y;
        y = z;
        z = (t ^ x) ^ y;
        return z;
    }

}
