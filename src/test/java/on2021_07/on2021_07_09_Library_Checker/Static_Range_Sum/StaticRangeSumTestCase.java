package on2021_07.on2021_07_09_Library_Checker.Static_Range_Sum;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class StaticRangeSumTestCase {
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
        int n = random.nextInt(1, 20);
        int m = random.nextInt(1, 20);
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(1, 10);
        }
        int[][] qs = new int[m][2];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < 2; j++){
                qs[i][j] = random.nextInt(0, n - 1);
            }
            if(qs[i][0] > qs[i][1]){
                SequenceUtils.swap(qs[i], 0, 1);
            }
            qs[i][1]++;
        }
        long[] ans = solve(a, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        for(int[] q : qs){
            printLine(in, q);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public long[] solve(int[] a, int[][] qs){
        LongPreSum ps = new LongPreSum(i -> a[i], a.length);
        LongArrayList ans = new LongArrayList();
        for(int[] q : qs){
            int l = q[0];
            int r = q[1] - 1;
            ans.add(ps.intervalSum(l, r));
        }
        return ans.toArray();
    }
}
