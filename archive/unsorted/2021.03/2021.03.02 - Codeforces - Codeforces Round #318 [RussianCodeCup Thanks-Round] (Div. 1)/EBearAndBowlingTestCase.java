package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.MonotoneOrderBeta;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class EBearAndBowlingTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100000; i++){
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
        int n = random.nextInt(1, 10);
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(-10, 10);
        }
        long ans = solve(n, a);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);

        return new Test(in.toString(), "" + ans);
    }

    long solve(int n, int[] a){
        MonotoneOrderBeta<Long, Long> mo = new MonotoneOrderBeta<Long, Long>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                false, true);
        mo.add(0L, 0L);
        List<long[]> lists = new ArrayList<>(n);
        a = a.clone();
        SequenceUtils.reverse(a);
        for (int x : a) {
            lists.clear();
            for (Map.Entry<Long, Long> entry : mo) {
                lists.add(new long[]{entry.getKey(), entry.getValue()});
            }
            for (long[] entry : lists) {
                mo.add(entry[0] + x, entry[1] + entry[0] + x);
            }
        }
        long ans = mo.first().getValue();
        return ans;
    }
}
