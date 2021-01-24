package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class XRXNTestCase {
    @TestCase
    public Collection<Test> createTests() {
        solve(4323);
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        int x = random.nextInt(1, 10000);
        int ans = solve(x);
        return new Test("" + x +"\n0", "" + ans);
    }

    public int solve(int x) {
        StringBuilder s = new StringBuilder();
        int ans = 0;
        for (int i = 1; i <= x; i++) {
            s.setLength(0);
            s.append(i);
            int rev = Integer.parseInt(s.reverse().toString());
            if(i + rev == x){
                ans++;
            }
        }
        return ans;
    }
}
