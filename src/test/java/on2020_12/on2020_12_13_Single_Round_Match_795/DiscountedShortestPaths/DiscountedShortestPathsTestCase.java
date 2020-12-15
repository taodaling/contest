package on2020_12.on2020_12_13_Single_Round_Match_795.DiscountedShortestPaths;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.task.TopCoderTask;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class DiscountedShortestPathsTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for(int i = 1; i <= 1000; i++){
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
        int n = 20;
        int[] a = new int[n * n];
        int[] b = new int[n * n];
        int[] c = new int[n * n];
        for(int i = 0; i < n * n; i++){
            a[i] = i / n ;
            b[i] = i % n;
            c[i] = random.nextInt(1, (int)1e9);
        }
        int[] d = new int[20];
        for(int i = 0; i < 20; i++){
            d[i] = random.nextInt(1, (int)100);
        }
        return new NewTopCoderTest(new Object[]{n, a, b, c, d});
    }
}
