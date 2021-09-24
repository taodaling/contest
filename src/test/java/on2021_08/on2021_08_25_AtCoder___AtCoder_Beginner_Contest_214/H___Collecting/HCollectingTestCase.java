package on2021_08.on2021_08_25_AtCoder___AtCoder_Beginner_Contest_214.H___Collecting;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HCollectingTestCase {
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
        int n = (int) 1e5;
        int m = n - 1;
        List<int[]> edges = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            edges.add(new int[]{random.nextInt(1, i - 1), i});
        }
        int[] xs = new int[n];
        for(int i = 0; i < n; i++){
            xs[i] = random.nextInt(1, (int)1e9);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, edges.size(), 10);
        for(int[] e : edges){
            printLine(in, e);
        }
        printLine(in, xs);
        return new Test(in.toString());
    }
}
