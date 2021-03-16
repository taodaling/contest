package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class JOISC2014Day1TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int n = random.nextInt(1, 5);
        int q = random.nextInt(1, 5);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, (int)10);
        }
        int[][] qs = new int[q][];
        for (int i = 0; i < q; i++) {
            qs[i] = new int[]{random.nextInt(1, n), random.nextInt(1, n)};
            if(qs[i][0] > qs[i][1]){
                SequenceUtils.swap(qs[i], 0, 1);
            }
        }
        long[] ans = solve(a, qs);
        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        printLine(in, a);
        for(int[] query : qs){
            printLine(in, query);
        }
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public long[] solve(int[] a, int[][] qs){
        long[] ans = new long[qs.length];
        for(int i = 0; i < qs.length; i++){
            int l = qs[i][0] - 1;
            int r = qs[i][1] - 1;
            Map<Integer, Long> map  = new HashMap<>();
            for(int j = l; j <= r; j++){
                map.put(a[j], map.getOrDefault(a[j], 0L) + a[j]);
            }
            ans[i] = map.values().stream().max(Comparator.naturalOrder()).get();
        }
        return ans;
    }
}
