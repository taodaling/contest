package on2020_07.on2020_07_19_2020_TopCoder_Open_Algo.FixedNumberOfDigits;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class FixedNumberOfDigitsTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
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

    public NewTopCoderTest create(int testNum) {
        int start = random.nextInt(0, 10);
        int step = random.nextInt(1, 10);
        int k = random.nextInt(1, (int) 5);
        return new NewTopCoderTest(new Object[]{start, step, k}, solve(start, step, k));
    }

    public long solve(int start, int step, int k) {
        for (int i = start; ; i += step) {
            String s = Integer.toString(i);
            if (k > s.length()) {
                k -= s.length();
                continue;
            }
            String ans = s.substring(0, k);
            return Long.parseLong(ans);
        }
    }
}
