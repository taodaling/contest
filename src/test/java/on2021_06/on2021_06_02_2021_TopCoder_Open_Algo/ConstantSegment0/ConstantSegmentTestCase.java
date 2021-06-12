package on2021_06.on2021_06_02_2021_TopCoder_Open_Algo.ConstantSegment0;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class ConstantSegmentTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<NewTopCoderTest> tests = new ArrayList<>();
        for(int i = 1; i <= 10000; i++){
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

    RandomWrapper random = new RandomWrapper(0);
    public NewTopCoderTest create(int testNum){
        int n = random.nextInt(1, 100);
        int k = random.nextInt(1, n);
        int m = random.nextInt(1, 3);
        int[] px = new int[0];
        int seed = random.nextInt(1, 100);
        return new NewTopCoderTest(new Object[]{n, k, m, px, seed});
    }
}
