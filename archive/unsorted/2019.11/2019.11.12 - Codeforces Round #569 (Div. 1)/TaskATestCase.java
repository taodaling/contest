package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskATestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(2, 5);
        int m = random.nextInt(1, 3);
        long[] data = new long[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextLong(1, (long) 1e18);
        }
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        in.append(n).append(' ').append(m).append('\n');
        for (int i = 0; i < n; i++) {
            in.append(data[i]).append(' ');
        }
        in.append('\n');
        for (int i = 0; i < m; i++) {
            int time = random.nextInt(1, 10);
            in.append(time).append(' ');
            long[] ans = solve(data, time);
            out.append(ans[0]).append(' ').append(ans[1]).append('\n');
        }
        return new Test(in.toString(), out.toString());
    }

    public long[] solve(long[] x, int t) {
        Deque<Long> deque = new ArrayDeque<>();
        for (long a : x) {
            deque.addLast(a);
        }
        for (int i = 0; i < t - 1; i++) {
            long a = deque.removeFirst();
            long b = deque.removeFirst();
            deque.addLast(Math.min(a, b));
            deque.addFirst(Math.max(a, b));
        }
        return new long[]{deque.removeFirst(), deque.removeFirst()};
    }
}
