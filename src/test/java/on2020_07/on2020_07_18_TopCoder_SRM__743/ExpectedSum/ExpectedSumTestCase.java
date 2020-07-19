package on2020_07.on2020_07_18_TopCoder_SRM__743.ExpectedSum;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class ExpectedSumTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
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
    public NewTopCoderTest create(int testNum){
        int[] ans = new int[50];
        int[] prob = new int[50];
        for(int i = 0; i < 50; i++){
            ans[i] = random.nextInt(0, 50);
            prob[i] = random.nextInt(0, 100);
        }
        return new NewTopCoderTest(new Object[]{ans, prob});
    }
}
