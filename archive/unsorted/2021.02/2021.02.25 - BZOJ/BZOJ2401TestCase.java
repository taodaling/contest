package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.test.ExternalTestLoader;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.LCMs;
import template.rand.RandomWrapper;

public class BZOJ2401TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
//        List<Test> tests = new ArrayList<>();
//        for(int i = 1; i <= 0; i++){
//            System.out.println("build  testcase " + i);
//            tests.add(create(i));
//        }
        return ExternalTestLoader.load();
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
        int n = testNum;
        return new Test("1\n" + n, "" + solve(n));
    }

    public long solve(int n){
        long ans = 0;
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= n; j ++){
                ans += LCMs.lcm(i, j);
            }
        }
        return ans;
    }
}
