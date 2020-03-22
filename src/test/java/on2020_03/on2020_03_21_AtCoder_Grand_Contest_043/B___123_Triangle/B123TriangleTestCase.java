package on2020_03.on2020_03_21_AtCoder_Grand_Contest_043.B___123_Triangle;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class B123TriangleTestCase {
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
        int n = random.nextInt(2, 50);
        int[] seq = new int[n];
        for (int i = 0; i < n; i++) {
            seq[i] = random.nextInt(1, 3);
        }
        int[] ans = seq.clone();
        for (int i = 1; i < n; i++) {
            cast(ans);
        }
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n);
        for (int i = 0; i < n; i++) {
            in.append(seq[i]);
        }
        return new Test(in.toString(), "" + ans[0]);
    }

    public void cast(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            a[i] = Math.abs(a[i] - a[i + 1]);
        }
    }


}
