package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.RandomWrapper;

public class P4168VioletTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("build  testcase " + i);
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(1, 10);
        }

        int[][] qs = new int[m][2];
        for (int i = 0; i < m; i++) {
            qs[i][0] = random.nextInt(1, 10);
            qs[i][1] = random.nextInt(1, 10);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int i = 0; i < n; i++) {
            in.append(data[i]).append(' ');
        }
        in.append(System.lineSeparator());
        for (int i = 0; i < m; i++) {
            printLine(in, qs[i][0], qs[i][1]);
        }
        int[] ans = solve(data, qs);
        StringBuilder out = new StringBuilder();
        for (int x : ans) {
            out.append(x).append(' ');
        }
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] data, int[][] qs) {
        int x = 0;
        IntegerList ans = new IntegerList();
        for (int[] q : qs) {
            int l = DigitUtils.mod(q[0] + x - 1, data.length);
            int r = DigitUtils.mod(q[1] + x - 1, data.length);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            Map<Integer, Integer> cnt = new HashMap<>();
            for (int j = l; j <= r; j++) {
                cnt.put(data[j], cnt.getOrDefault(data[j], 0) + 1);
            }
            x = cnt.entrySet().stream()
                    .sorted((a, b) -> a.getValue().equals(b.getValue()) ?
                            a.getKey().compareTo(b.getKey()) : -a.getValue().compareTo(b.getValue()))
                    .map(t -> t.getKey()).findFirst().get();
            ans.add(x);
        }
        return ans.toArray();
    }
}
