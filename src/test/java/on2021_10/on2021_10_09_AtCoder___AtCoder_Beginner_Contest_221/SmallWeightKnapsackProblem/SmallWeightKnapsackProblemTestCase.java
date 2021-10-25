package on2021_10.on2021_10_09_AtCoder___AtCoder_Beginner_Contest_221.SmallWeightKnapsackProblem;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class SmallWeightKnapsackProblemTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
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
            a[i] = random.nextInt(1, 10);
        }
        int sum = Arrays.stream(a).sum();
        int C = random.nextInt(0, sum);
        int ans = solve(a, C);
        StringBuilder in = new StringBuilder();
        printLine(in, n, C);
        printLine(in, a);
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[] w, int c){
        int best = 0;
        for(int i = 0; i < 1 << w.length; i++){
            int s = 0;
            for(int j = 0; j < w.length; j++){
                s += Bits.get(i, j) * w[j];
            }
            if(s <= c){
                best = Math.max(best, s);
            }
        }
        return best;
    }
}
