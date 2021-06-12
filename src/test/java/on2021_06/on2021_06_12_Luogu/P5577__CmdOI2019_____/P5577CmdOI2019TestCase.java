package on2021_06.on2021_06_12_Luogu.P5577__CmdOI2019_____;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import chelper.ExternalExecutor;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.polynomial.ModGenericFastWalshHadamardTransform;
import template.rand.RandomWrapper;

public class P5577CmdOI2019TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        int n = (int) 10;
        int k = 4;
        int m = 6;
        StringBuilder in = new StringBuilder();
        printLine(in, n, k, m);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            String s = random.nextString('0', '3', m);
            printLineObj(in, s);
            a[i] = Integer.parseInt(s);
        }
        int[] ans = solve(a, k, m);
//        String out = new ExternalExecutor("F:\\geany\\main.exe").invoke(in.toString());
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }
    int k;

    public int asK(int x) {
        if (x == 0) {
            return 0;
        }
        return asK(x / 10) * k + x % 10;
    }

    int mod = 998244353;
    public int[] solve(int[] a, int k, int m) {
        int km = 1;
        for(int i = 0; i < m; i++){
            km *= k;
        }
        this.k = k;
        int[] C = new int[km];
        for(int x : a){
            C[asK(x)]++;
        }
        ModGenericFastWalshHadamardTransform fwt = new ModGenericFastWalshHadamardTransform(k, mod);
        fwt.disableG();
        int[] ans = fwt.countSubset(C);
        return ans;
    }
}
