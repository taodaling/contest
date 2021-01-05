package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class ERandomElectionsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = 20;
        int[] val = new int[1 << n];
        for (int i = 0; i < (1 << n); i++) {
            val[i] = i < (1 << (n - 1)) ? 1 : 0;
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int i = 0; i < (1 << n); i++){
            in.append(val[i]);
        }

        return new Test(in.toString(), null);
    }


}
