package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskFTestCase {
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
        int n = random.nextInt(3, 5);
        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = random.nextInt(1, 10);
        }
        long ans = solve(s);
        StringBuilder builder = new StringBuilder();
        builder.append(n).append('\n');
        for (int i = 0; i < n; i++) {
            builder.append(s[i]).append(' ');
        }
        return new Test(builder.toString(), "" + ans);
    }

    public long solve(int[] s) {
        int n = s.length;
        long ans = (long) -1e18;
        for (int i = 2; i < n; i++) {
            for (int j = 1; j < i; j++) {
                boolean[] visited = new boolean[n];
                long local = 0;
                int pos = 0;
                while (pos != n - 1) {
                    visited[pos] = true;
                    if (pos + i < 0 || pos + i >= n || visited[pos + i]) {
                        local = (long) -1e18;
                        break;
                    }
                    pos += i;
                    visited[pos] = true;
                    local += s[pos];
                    if(pos == n - 1){
                        break;
                    }
                    if (pos - j < 0 || pos - j >= n || visited[pos - j]) {
                        local = (long) -1e18;
                        break;
                    }
                    pos -= j;
                    visited[pos] = true;
                    local += s[pos];
                    if(pos == n - 1){
                        break;
                    }
                }

                ans = Math.max(ans, local);
            }
        }

        return ans;
    }
}
