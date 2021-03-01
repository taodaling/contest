package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int p = random.nextInt(1, 15);
        int[] a = new int[n];
        a[0] = random.nextInt(1, 10);
        for(int i = 1; i < n; i++){
            a[i] = a[i - 1] + random.nextInt(1, a[0]);
        }
        String ans = solve(a, p);
        StringBuilder in = new StringBuilder();
        printLine(in, a.length, p);
        printLine(in, a);
        return new Test(in.toString(), ans);
    }

    int[][] dp;
    int[] a;

    public boolean dp(int s, int p){
        if(dp[s][p] == -1){
            boolean ans = false;
            for(int i = 0; i < a.length; i++){
                if(Bits.get(s, i) == 0 || p < a[i]){
                    continue;
                }
                if(!dp(Bits.clear(s, i), p - a[i])){
                    ans = true;
                }
            }
            dp[s][p] = ans ? 1 : 0;
        }
        return dp[s][p] == 1;
    }

    public String solve(int[] a, int p){
        this.a = a;
        dp = new int[1 << a.length][p + 1];
        SequenceUtils.deepFill(dp, -1);
        boolean ans = dp((1 << a.length) - 1, p);
        return ans ? "F" : "S";
    }
}
