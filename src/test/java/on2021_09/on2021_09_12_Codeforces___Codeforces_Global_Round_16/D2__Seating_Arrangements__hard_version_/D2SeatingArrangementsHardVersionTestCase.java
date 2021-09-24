package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.D2__Seating_Arrangements__hard_version_;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.PermutationUtils;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class D2SeatingArrangementsHardVersionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        int[] a = new int[n * m];
        for (int i = 0; i < n * m; i++) {
            a[i] = random.nextInt(1, 9);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, m);
        printLine(in, a);
        int ans = solve(n, m, a);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int n, int m, int[] a) {
        int nm = n * m;
        int[] indices = IntStream.range(0, nm).toArray();
        int best = (int) 1e9;
        do {
            boolean ok = true;
            for (int i = 1; i < nm; i++) {
                if (a[indices[i - 1]] > a[indices[i]]) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }
            int cost = 0;
            for (int i = 0; i < n; i++) {
                int start = i * m;
                for (int j = 0; j < m; j++) {
                    int id = i * m + j;
                    for (int k = start; k < id; k++) {
                        if (indices[k] < indices[id]) {
                            cost++;
                        }
                    }
                }
            }
            best = Math.min(cost, best);
        } while (PermutationUtils.nextPermutation(indices));
        return best;
    }
}
