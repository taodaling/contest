package on2021_04.on2021_04_24_.Top_Row;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TopRowTestCase {
    @TestCase
    public Collection<Test> createTests() {
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
        StringBuilder in = new StringBuilder();
        int n = (int) 1e5;
        String s = random.nextString('a', 'z', n);
        printLineObj(in, s);
        int q = (int) 1e5;
        printLine(in, q);
        for (int i = 0; i < q; i++) {
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            printLine(in, l, r);
        }
        return new Test(in.toString(), null);
    }
}
