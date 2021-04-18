package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Median_Sort0;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class MedianSortTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
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
        int n = random.nextInt(50, 50);
        int[] value = IntStream.range(0, n).toArray();
        Randomized.shuffle(value);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, value);
        return new Test(in.toString(), null);
    }
}
