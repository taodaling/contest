package on2021_07.on2021_07_10_Kattis.CountStringContainSubstringTest;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.math.Power;
import template.rand.RandomWrapper;
import template.string.ACAutomaton;
import template.string.KMPAutomaton;

public class CountStringContainSubstringTestTestCase {
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
        int n = random.nextInt(1000000, 1000000);
        int c = random.nextInt(1, 26);
        int m = random.nextInt(1000000, n);
        char[] s = new char[m];
        for (int i = 0; i < m; i++) {
            s[i] = (char) random.nextInt('a', 'a' + c - 1);
        }

        int ans = 0;//solve(n, c, s);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, c, m);
        printLineObj(in, new String(s));
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public int solve(int n, int c, char[] s) {
        int m = s.length;
        ACAutomaton ac = new ACAutomaton('a', 'z', m);
        for (char ch : s) {
            ac.build(ch);
        }
        int[] topo = ac.endBuild();
        long[] prev = new long[topo.length];
        long[] next = new long[topo.length];
        prev[0] = 1;
        for (int r = 0; r < n; r++) {
            Arrays.fill(next, 0);
            prev[topo.length - 1] = 0;
            for (int j = 0; j < topo.length; j++) {
                for (int nc = 0; nc < c; nc++) {
                    next[ac.next[nc][j]] += prev[j];
                }
            }
            for(int j = 0; j < topo.length; j++){
                next[j] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        prev[topo.length - 1] = 0;
        long ans = pow.pow(c, n) - Arrays.stream(prev).sum() % mod;
        return DigitUtils.mod(ans, mod);
    }
}
