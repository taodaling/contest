package on2021_09.on2021_09_26_AtCoder___AtCoder_Regular_Contest_127.D___Sum_of_Min_of_Xor;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DSumOfMinOfXorTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
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

    private <T> void printLineObj(StringBuilder builder, T... vals) {
            for (T val : vals) {
                builder.append(val).append(' ');
            }
            builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        int n = random.nextInt(1, 10);
        int B = 18;
        int[] a = new int[n];
        int[] b = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(0, (1 << B) - 1);
            b[i] = random.nextInt(0, (1 << B) - 1);
        }
        long ans = solve(n, a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        printLine(in, b);
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int n, int[] a, int[] b){
        long ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                ans += Math.min(a[i] ^ a[j], b[i] ^ b[j]);
            }
        }
        return ans;
    }
}
