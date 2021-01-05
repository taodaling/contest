package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(0, 3) * 2 + 1;
        char[] c = new char[2 * n];
        for (int i = 0; i < n; i++) {
            c[i] = 'R';
        }
        for (int i = 0; i < n; i++) {
            c[i + n] = 'B';
        }
        Randomized.randomizedArray(c, 0, c.length);
        StringBuilder builder = new StringBuilder();
        builder.append(n).append('\n').append(String.valueOf(c));
        return new Test(builder.toString(), "");
    }
}
