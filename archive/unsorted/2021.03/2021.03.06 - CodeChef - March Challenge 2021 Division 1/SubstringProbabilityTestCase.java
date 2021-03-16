package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Power;
import template.rand.RandomWrapper;

public class SubstringProbabilityTestCase {
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
        int n = random.nextInt(1, 5);
        int m = random.nextInt(1, 100);
        String p = random.nextString('a', 'z', n);
        String q = random.nextString('a', 'z', m);
        long ans = solve(p, q);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLineObj(in, p);
        printLineObj(in, q);
        return new Test(in.toString(), "" + ans);
    }


    public long solve(String a, String b) {
        int way = 0;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= a.length(); j++) {
                String merge = a.substring(0, i) + a.substring(a.length() - j);
                if (b.indexOf(merge) >= 0) {
                    way++;
                }
            }
        }
        int total = a.length() * a.length();
        int mod = 998244353;
        Power pow = new Power(mod);
        long ans = (long)way * pow.inverse(total) % mod;
        return ans;
    }
}
