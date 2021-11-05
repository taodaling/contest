package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class HXorQueryTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int...vals){
        for(int val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long...vals){
        for(long val : vals){
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
    public Test create(int testNum){
        int n = (int)2e5;
        int q = (int)1e5;
        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        long[] a = new long[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextLong(0, (1L << 60) - 1);
        }
        printLine(in, a);
        for(int i = 0; i < q; i++){
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            long x = random.nextLong(0, (1L << 60) - 1);
            printLine(in, l, r, x);
        }
        return new Test(in.toString());
    }
}
