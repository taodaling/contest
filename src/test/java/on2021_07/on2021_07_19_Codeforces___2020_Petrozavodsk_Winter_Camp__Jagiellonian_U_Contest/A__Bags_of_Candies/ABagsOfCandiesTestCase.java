package on2021_07.on2021_07_19_Codeforces___2020_Petrozavodsk_Winter_Camp__Jagiellonian_U_Contest.A__Bags_of_Candies;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.GeneralGraphMatchingByBlossom;
import template.math.GCDs;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ABagsOfCandiesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 2; i <= 100; i++) {
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
        return new Test("1\n" + testNum, "" + solve(testNum));
    }

    public int solve(int n) {
        GeneralGraphMatchingByBlossom byBlossom = new GeneralGraphMatchingByBlossom(n + 1);
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (GCDs.gcd(i, j) > 1) {
                    byBlossom.addEdge(i, j);
                }
            }
        }
        return n - byBlossom.maxMatch(true);
    }
}
