package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DINOIFinalContestsTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 4);
        int m = random.nextInt(1, n);
        int p = (int) 1e9 + 7;
        long ans = solve(n, m, p);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, p);
        return new Test(in.toString(), "" + ans);
    }

    int p;

    public long dfs(int[] dir, int[] prefer, int people, int m) {
        if (people == -1) {
            boolean[] visited = new boolean[m];
            long sum = 0;
            for (int i = 0; i < dir.length; i++) {
                boolean find = false;
                if (dir[i] == 0) {
                    //l to r
                    for (int j = prefer[i]; j < m; j++) {
                        if (!visited[j]) {
                            visited[j] = true;
                            sum += j - prefer[i];
                            find = true;
                            break;
                        }
                    }
                } else {
                    for (int j = prefer[i]; j >= 0; j--) {
                        if (!visited[j]) {
                            visited[j] = true;
                            sum += prefer[i] - j;
                            find = true;
                            break;
                        }
                    }
                }
                if (!find) {
                    return 0;
                }
            }
            return sum;
        }
        long sum = 0;
        for (int i = 0; i <= 1; i++) {
            dir[people] = i;
            for (int j = 0; j < m; j++) {
                prefer[people] = j;
                sum += dfs(dir, prefer, people - 1, m);
            }
        }
        return sum % p;
    }

    public long solve(int n, int m, int p) {
        this.p = p;
        return dfs(new int[m], new int[m], m - 1, n);
    }
}
