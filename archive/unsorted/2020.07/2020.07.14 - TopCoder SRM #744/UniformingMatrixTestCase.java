package contest;

import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.task.TopCoderTask;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class UniformingMatrixTestCase {
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
        char[][] mat = new char[100][100];
        for(int i = 0; i < 100; i++){
            for(int j = 0; j < 100; j++){
                mat[i][j] = (char) (random.nextInt(0, 1) + '0');
            }
        }
        String[] ans = Arrays.stream(mat).map(String::valueOf).toArray(x -> new String[x]);
        return new NewTopCoderTest(new Object[]{ans});
    }
}
