package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class ParentingPartneringReturnsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        StringBuilder builder = new StringBuilder();
        printLine(builder, 1);
        int n = random.nextInt(1, 10);
        printLine(builder, n);
        for (int i = 0; i < n; i++) {
            int l = random.nextInt(0, 10);
            int r = random.nextInt(0, 10);
            if(l == r){
                r++;
            }
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            printLine(builder, l, r);
        }
        return new Test(builder.toString(), "");
    }


}
