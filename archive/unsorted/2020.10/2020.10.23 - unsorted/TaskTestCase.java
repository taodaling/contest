package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.Power;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
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

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        int n = random.nextInt(1, 1000);
        int k = random.nextInt(1, 100);
        return new Test(String.format("%d %d", n, k), "" + solve(n, k));
    }

    public int solve(int n, int k){
        long ans = 0;
        int mod = (int) (1e9 + 7);
        Power pow = new Power(mod);
        for(int i = 1; i <= n; i++){
            ans += pow.pow(i, k);
        }
        return (int) (ans % mod);
    }
}
