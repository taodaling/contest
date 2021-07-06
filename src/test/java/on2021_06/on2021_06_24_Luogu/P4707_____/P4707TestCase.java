package on2021_06.on2021_06_24_Luogu.P4707_____;



import java.util.*;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Power;
import template.rand.RandomWrapper;

public class P4707TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        solve(2, 1, 6, new int[]{3, 3});
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
        int n = random.nextInt(1, 3);
        int k = random.nextInt(1, n);
        int[] p = new int[n];
        for(int i = 0; i < n; i++){
            p[i] = random.nextInt(1, 3);
        }
        int m = Arrays.stream(p).sum();
        //long ans = solve(n, k, m, p);
        StringBuilder in = new StringBuilder();
        printLine(in, n, k, m);
        printLine(in, p);
        String ans = new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString());
        return new Test(in.toString(), "" + ans);
    }

    int mod = 998244353;

    Combination comb = new Combination((int)1000, mod);
    Power pow = new Power(mod);
    public long solve(int n, int k, int m, int[] p){
        int kthLargest = n - k + 1;
        long ans = 0;
        for(int i = 1; i < 1 << n; i++){
            int T = Integer.bitCount(i);
            int sum = 0;
            for(int j = 0; j < n; j++){
                if(Bits.get(i, j) == 1){
                    sum += p[j];
                }
            }

            long contrib = (long)comb.combination(T - 1, kthLargest - 1) *
                    m % mod * pow.inverse(sum) % mod;
            if((T - kthLargest) % 2 != 0){
                contrib = -contrib;
            }
            ans += contrib;
        }
        return DigitUtils.mod(ans, mod);
    }


}
