package on2021_09.on2021_09_16_CS_Academy___Round__41.Add_and_Subtract;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class AddAndSubtractTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
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
            a[i] = random.nextInt(-50, 50);
        }
        long[] ans = solve(a, n);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, a);
        StringBuilder out = new StringBuilder();
        for(int i = 1; i <= n; i++){
            out.append(ans[i]).append(' ');
        }
        return new Test(in.toString(), out.toString());
    }

    public long[] solve(int[] a, int n){
        long[] ans = new long[n + 1];
        long inf = (long)1e18;
        Arrays.fill(ans, -inf);
        for(int i = 0; i < 1 << n; i++){
            int sgn = 1;
            long sum = 0;
            for(int j = 0; j < n; j++){
                if(Bits.get(i, j) == 0){
                    continue;
                }
                sum += a[j] * sgn;
                sgn = -sgn;
            }
            int bc = Integer.bitCount(i);
            ans[bc] = Math.max(ans[bc], sum);
        }
        return ans;
    }
}
