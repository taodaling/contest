package on2020_08.on2020_08_31_Codeforces___AIM_Tech_Round_3__Div__1_.B__Recover_the_String;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class BRecoverTheStringTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(random.nextInt(0, 1));
        }
        StringBuilder in = new StringBuilder();
        int[] ans = count(sb.toString());
        for (int i = 0; i < 4; i++) {
            in.append(ans[i]).append(' ');
        }
        return new Test(in.toString(), null);
    }

    public int[] count(String s) {
        int[] pre = new int[2];
        int[] ans = new int[4];
        for (char c : s.toCharArray()) {
            if (c == '0') {
                ans[0] += pre[0];
                ans[2] += pre[1];
            } else {
                ans[1] += pre[0];
                ans[3] += pre[1];
            }
            pre[c - '0']++;
        }
        return ans;
    }
}
