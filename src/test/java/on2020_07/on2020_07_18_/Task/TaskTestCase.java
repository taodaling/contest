package on2020_07.on2020_07_18_.Task;



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
        for (int i = 0; i < 1000; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int w = random.nextInt(0, Integer.MAX_VALUE - 1);
        int x = random.nextInt(0, Integer.MAX_VALUE - 1);
        int y = random.nextInt(0, Integer.MAX_VALUE - 1);
        int z = random.nextInt(0, (int)1e6);
        String in = String.format("%d %d %d %d", w, x, y, z);
        String out = "" + find(x, w, y, z);
        return new Test(in, out);
    }
    BitSet bs = new BitSet(Integer.MAX_VALUE);
    public int find(long x, long w, long y, int z) {
        int m = (1 << 31) - 1;
        bs.clear();
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
