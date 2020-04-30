package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class FFunctionTestCase {
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
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        int[] a = new int[n];
        int[] b = new int[m];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        for (int i = 0; i < m; i++) {
            b[i] = i;
        }
        Randomized.shuffle(a);
        Randomized.shuffle(b);

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        for (int i = 0; i < n; i++) {
            in.append(a[i]).append(' ');
        }
        printLine(in);
        for (int i = 0; i < m; i++) {
            in.append(b[i]).append(' ');
        }
        printLine(in);
        out.append("Case #1: ").append(solve(a, b));
        return new Test(in.toString(), out.toString());
    }

    public boolean check(int[] f, int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (f[i] != b[f[a[i]]]) {
                return false;
            }
        }
        return true;
    }

    public int dfs(int[] a, int[] b, int[] f, int i) {
        if (i < 0) {
            return check(f, a, b) ? 1 : 0;
        }
        int ans = 0;
        for (int j = 0; j < b.length; j++) {
            f[i] = j;
            ans += dfs(a, b, f, i - 1);
        }
        return ans;
    }

    public int solve(int[] a, int[] b) {
        return dfs(a, b, new int[a.length], a.length - 1);
    }
}
