package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.E__Minimax;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.PermutationUtils;
import template.rand.RandomWrapper;
import template.string.KMPAutomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EMinimaxTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        solve("aacad");
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
        int n = random.nextInt(1, 12);
        String input = random.nextString('a', 'd', n);
        return new Test("1\n" + input, solve(input));
    }

    public String solve(String s) {
        int[] c = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            c[i] = s.charAt(i);
        }
        Arrays.sort(c);
        KMPAutomaton kmp = new KMPAutomaton(s.length());
        String bestChoice = s;
        int occur = s.length();
        do {
            kmp.init();
            for (int x : c) {
                kmp.build(x);
            }
            int max = 0;
            for (int i = 1; i < s.length(); i++) {
                int b = kmp.maxBorder(i);
                max = Math.max(max, b);
            }
            if (max < occur) {
                occur = max;
                char[] cast = new char[s.length()];
                for (int i = 0; i < cast.length; i++) {
                    cast[i] = (char) c[i];
                }
                bestChoice = new String(cast);
            }
        }
        while (PermutationUtils.nextPermutation(c));
        return bestChoice;
    }
}
