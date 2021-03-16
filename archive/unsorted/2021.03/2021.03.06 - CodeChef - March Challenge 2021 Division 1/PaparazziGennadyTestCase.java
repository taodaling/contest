package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.geometry.geo2.IntegerPoint2;
import template.rand.RandomWrapper;

public class PaparazziGennadyTestCase {
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
        int n = random.nextInt(1, 10);
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = random.nextInt(1, (int) 1e9);
        }
        int ans = solve(h);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        printLine(in, h);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[] h) {
        IntegerPoint2[] pts = new IntegerPoint2[h.length];
        for (int i = 0; i < h.length; i++) {
            pts[i] = new IntegerPoint2(i + 1, h[i]);
        }
        int ans = 0;
        for (int i = 0; i < h.length; i++) {
            for(int j = i + 1; j < h.length; j++){
                boolean ok = true;
                for(int k = i + 1; k < j; k++){
                    if(IntegerPoint2.cross(pts[i], pts[j], pts[k]) > 0){
                        ok = false;
                    }
                }
                if(ok){
                    ans = Math.max(ans, j - i);
                }
            }
        }
        return ans;
    }
}
