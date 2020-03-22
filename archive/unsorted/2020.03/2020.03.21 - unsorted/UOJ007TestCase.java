package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class UOJ007TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 50);
        List<long[]> nodes = new ArrayList<>(n - 1);
        for (int i = 0; i < n - 1; i++) {
            int f = random.nextInt(0, i) + 1;
            int s = random.nextInt(1, 10);
            int p = random.nextInt(1, 10);
            int q = random.nextInt(1, 10);
            int len = random.nextInt(1, 10);
            if (len < s) {
                int tmp = len;
                len = s;
                s = tmp;
            }
            nodes.add(new long[]{f, s, p, q, len});
        }

        long[] ans = solve(nodes);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, 1);
        for (long[] args : nodes) {
            printLine(in, args[0], args[1], args[2], args[3], args[4]);
        }

        for (int i = 1; i < n; i++) {
            printLine(out, ans[i]);
        }

        return new Test(in.toString(), out.toString());
    }


    public long[] solve(List<long[]> info) {
        int n = info.size() + 1;
        int[] fa = new int[n];
        fa[0] = -1;
        long[] depth = new long[n];
        long[] a = new long[n];
        long[] b = new long[n];
        long[] len = new long[n];
        for (int i = 1; i <= n - 1; i++) {
            long[] arg = info.get(i - 1);
            fa[i] = (int) arg[0] - 1;
            depth[i] = arg[1] + depth[fa[i]];
            a[i] = arg[2];
            b[i] = arg[3];
            len[i] = arg[4];
        }
        long[] dp = new long[n];
        Arrays.fill(dp, (long)2e18);
        dp[0] = 0;
        for (int i = 1; i < n; i++) {
            int trace = fa[i];
            while (trace >= 0 && depth[i] - depth[trace] <= len[i]) {
                dp[i] = Math.min(dp[i], dp[trace] + (depth[i] - depth[trace]) * a[i] + b[i]);
                trace = fa[trace];
            }
        }
        return dp;
    }
}
