package on2021_07.on2021_07_13_Single_Round_Match_809.TruckUnion0;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class TruckUnionTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<NewTopCoderTest> tests = new ArrayList<>();
        for(int i = 1; i <= 0; i++){
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
        int n = 18;
        int[] ans = new int[n * n];
        for(int i = 0; i < n; i++){
            for(int j = i; j < n; j++){
                if(i == j){
                    ans[i * n + j] = 0;
                }else{
                    ans[i * n + j] = ans[j * n + i] = random.nextInt(1, 1000);
                }
            }
        }
        return new NewTopCoderTest(new Object[]{n, ans});
    }
}
