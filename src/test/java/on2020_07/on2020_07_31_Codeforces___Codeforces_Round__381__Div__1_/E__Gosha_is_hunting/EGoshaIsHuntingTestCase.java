package on2020_07.on2020_07_31_Codeforces___Codeforces_Round__381__Div__1_.E__Gosha_is_hunting;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EGoshaIsHuntingTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        StringBuilder in = new StringBuilder();
        int n = 2000;
        int m = 2000;
        printLine(in, n, m, m);
        for (int i = 0; i < m; i++) {
            in.append(String.format("%.3f ", random.nextDouble(0, 1)));
        }
        printLine(in);
        for(int i = 0; i < m; i++){
            in.append(String.format("%.3f ", random.nextDouble(0, 1)));
        }
        return new Test(in.toString(), null);
    }
}
