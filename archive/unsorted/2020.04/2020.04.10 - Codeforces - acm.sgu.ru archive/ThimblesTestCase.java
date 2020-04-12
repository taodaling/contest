package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class ThimblesTestCase {
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
        int n = random.nextInt(2, 5);
        int m = random.nextInt(1, 10);
        int[][] ops = new int[m][2];
        for (int i = 0; i < m; i++) {
            int a = random.nextInt(1, n);
            int b = random.nextInt(1, n);
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            if (a == b) {
                i--;
                continue;
            }
            ops[i][0] = a;
            ops[i][1] = b;
        }

        List<Integer> ans = solve(n, ops);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int[] op : ops){
            printLine(in, op[0], op[1]);
        }
        StringBuilder out = new StringBuilder();
        for(int x : ans){
            out.append(x).append(' ');
        }
        return new Test(in.toString(), out.toString());
    }

    private void dfs(int[][] ops, int[] perm, boolean[] used, boolean[] visited, int i) {
        if (i < 0) {
            int n = visited.length;
            boolean[] status = new boolean[n];
            status[1] = true;
            for (int p : perm) {
                SequenceUtils.swap(status, ops[p][0], ops[p][1]);
            }
            for (int j = 0; j < n; j++) {
                visited[j] = visited[j] || status[j];
            }
            return;
        }

        for (int j = 0; j < used.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            perm[i] = j;
            dfs(ops, perm, used, visited, i - 1);
            used[j] = false;
        }
    }

    public List<Integer> solve(int n, int[][] ops) {
        int m = ops.length;
        boolean[] visited = new boolean[n + 1];
        dfs(ops, new int[m], new boolean[m], visited, m - 1);
        List<Integer> ans = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (visited[i]) {
                ans.add(i);
            }
        }
        return ans;
    }
}
