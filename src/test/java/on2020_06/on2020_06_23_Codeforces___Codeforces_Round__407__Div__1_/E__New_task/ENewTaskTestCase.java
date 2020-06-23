package on2020_06.on2020_06_23_Codeforces___Codeforces_Round__407__Div__1_.E__New_task;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class ENewTaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            System.out.println("build  testcase " + i);
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
        int n = (int) 1e5;
        int[] data = new int[n];
        for(int i = 0; i < n; i++){
            data[i] = random.nextInt(1, 1000);
        }
        int m = (int)1e5;
        int[][] update = new int[m][2];
        for(int i = 0; i < m; i++){
            update[i][0] = random.nextInt(1, 2);
            update[i][1] = random.nextInt(1, n);
        }

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int i = 0; i < n; i++){
            in.append(data[i]).append(' ');
        }
        printLine(in);
        printLine(in, m);
        for(int i = 0; i < m; i++){
            printLine(in, update[i][0], update[i][1]);
        }
        return new Test(in.toString(), null);
    }
}
