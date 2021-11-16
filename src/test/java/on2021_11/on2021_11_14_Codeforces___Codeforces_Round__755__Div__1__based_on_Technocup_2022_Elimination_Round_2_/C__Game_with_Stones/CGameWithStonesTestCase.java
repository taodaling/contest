package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.C__Game_with_Stones;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CGameWithStonesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(0, 5);
        }
        long ans = solve(a);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        printLine(in, a);

        return new Test(in.toString(), "" + ans);
    }

    public long solve(int[] a) {
        long ans = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                int[] b = a.clone();
                boolean ok = true;
                for (int k = i; k < j; k++) {
                    if (b[k] < 0) {
                        ok = false;
                        break;
                    }
                    b[k + 1] -= b[k];
                    b[k] = 0;
                }
                if (b[j] != 0) {
                    ok = false;
                }
                if (ok) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
