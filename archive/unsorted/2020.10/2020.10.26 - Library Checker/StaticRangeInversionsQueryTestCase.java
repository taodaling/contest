package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class StaticRangeInversionsQueryTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int limit = 100;
        int n = random.nextInt(1, limit);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(1, limit);
        }
        int m = random.nextInt(1, limit);
        int[][] queries = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                queries[i][j] = random.nextInt(0, n - 1);
            }
            if (queries[i][0] > queries[i][1]) {
                SequenceUtils.swap(queries[i], 0, 1);
            }
            queries[i][1]++;
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, data);
        for (int[] q : queries) {
            printLine(in, q);
        }

        StringBuilder out = new StringBuilder();
        printLine(out, solve(data, queries));

        return new Test(in.toString(), out.toString());
    }

    public long inversePair(int[] a) {
        int m = IntegerDiscreteMap.discrete(a);
        IntegerBIT bit = new IntegerBIT(m);
        long ans = 0;
        for (int x : a) {
            ans += bit.query(x + 2, m);
            bit.update(x + 1, 1);
        }
        return ans;
    }

    public long[] solve(int[] a, int[][] qs) {
        int n = a.length;
        int m = qs.length;
        long[] ans = new long[m];
        for (int i = 0; i < m; i++) {
            int l = qs[i][0];
            int r = qs[i][1] - 1;
            ans[i] = inversePair(Arrays.copyOfRange(a, l, r + 1));
        }
        return ans;
    }
}
