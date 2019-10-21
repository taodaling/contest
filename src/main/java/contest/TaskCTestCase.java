package contest;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;

import java.util.*;

public class TaskCTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create());
        }
        return tests;
    }

    Random random = new Random(0);

    public Test create() {
        int n = random.nextInt(1000) + 1;
        int p = random.nextInt(1000000000) + 1;
        int[] time = new int[n];
        for (int i = 0; i < n; i++) {
            time[i] = random.nextInt(1000000001);
        }

        long[] ans = solve(time, p);


        StringBuilder in = new StringBuilder();
        in.append(n).append(' ').append(p).append('\n');
        for (int t : time) {
            in.append(t).append(' ');
        }

        StringBuilder out = new StringBuilder();
        for (long t : ans) {
            out.append(t).append(' ');
        }

        return new Test(in.toString(), out.toString());
    }

    public long[] solve(int[] times, int p) {
        int n = times.length;
        long[] ans = new long[n];
        boolean[] visited = new boolean[n];
        long t = 0;
        for (int i = 0; i < n; i++) {
            boolean used = false;
            for (int j = 0; j < n; j++) {
                if (visited[j]) {
                    continue;
                }
                if (times[j] <= t) {
                    used = true;
                    visited[j] = true;
                    ans[j] = t + p;
                    t = ans[j];
                    break;
                }
            }
            if (!used) {
                long min = Long.MAX_VALUE;
                for (int j = 0; j < n; j++) {
                    if (visited[j]) {
                        continue;
                    }
                    min = Math.min(min, times[j]);
                }
                t = min;
                i--;
            }
        }

        return ans;
    }
}
