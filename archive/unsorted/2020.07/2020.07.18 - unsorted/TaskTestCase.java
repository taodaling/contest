package contest;



import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        int m = 1 << random.nextInt(0, 20);
        int w = random.nextInt(0, m - 1);
        int x = random.nextInt(0, m - 1);
        int y = random.nextInt(0, m);
        int z = random.nextInt(0, (int)1e6);
        String in = String.format("%d %d %d %d %d", w, x, y, z, m);
        String out = "" + find(x, w, y, z, m);
        return new Test(in, out);
    }

    public int find(long x, long w, long y, int z, int m) {
        BitSet bs = new BitSet(m);
        m--;
        long val = w;
        for (int i = 0; ; i++) {
            if (i >= z || bs.get((int) (val & m))) {
                return i;
            }
            bs.set((int) (val & m));
            val = val * x + y;
        }
    }
}
