package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FYetAnotherMinimizationProblemTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object...vals){
        for(Object val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int n = 100000;
        int m = 20;
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int i = 0; i < n; i++){
            in.append(random.nextInt(1, 2)).append(' ');
        }
        return new Test(in.toString(), null);
    }


}
