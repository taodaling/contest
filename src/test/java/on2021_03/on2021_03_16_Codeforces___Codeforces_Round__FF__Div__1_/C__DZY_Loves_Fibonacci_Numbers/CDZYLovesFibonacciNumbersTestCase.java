package on2021_03.on2021_03_16_Codeforces___Codeforces_Round__FF__Div__1_.C__DZY_Loves_Fibonacci_Numbers;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CDZYLovesFibonacciNumbersTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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

    int mod = (int) 1e9 + 9;

    public Test create(int testNum) {
//        int n = random.nextInt(1, 4);
//        int m = random.nextInt(1, 4);
//        int[] a = new int[n];
//        for(int i = 0; i < n; i++){
//            a[i] = random.nextInt()
//        }
        return null;
    }
}
