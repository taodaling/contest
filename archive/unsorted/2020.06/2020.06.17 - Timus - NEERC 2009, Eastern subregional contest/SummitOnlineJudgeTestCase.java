package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.BitSet;
import template.rand.RandomWrapper;

public class SummitOnlineJudgeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int x = random.nextInt(1, 100);
        int y = random.nextInt(1, 100);
        if (x > y) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        int l = random.nextInt(1, 10000);
        int r = random.nextInt(1, 10000);
        if (l > r) {
            int tmp = l;
            l = r;
            r = tmp;
        }

        int ans = solve(x, y, l, r);
        StringBuilder in = new StringBuilder();
        printLine(in, x, y, l, r);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int x, int y, int l, int r) {
        BitSet bs = new BitSet(r + 1);
        bs.set(0);
        for (int i = 1; i <= r; i++) {
            int rangeL = Math.max(0, i - y);
            int rangeR = i - x;
            if (rangeL > rangeR) {
                continue;
            }
            if (bs.size(rangeL, rangeR) > 0) {
                bs.set(i);
            }
        }
        return bs.size(l, r);
    }
}
