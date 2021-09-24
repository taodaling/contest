package on2021_08.on2021_08_01_Codeforces___Codeforces_Round__736__Div__1_.D1__Gregor_and_the_Odd_Cows__Easy_;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class D1GregorAndTheOddCowsEasyTestCase {
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
        int n = random.nextInt(6000, 6000);
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = random.nextInt(0, (int) 1e7);
            }
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int[] pt : pts){
            printLine(in, pt);
        }
        return new Test(in.toString());
    }
}
