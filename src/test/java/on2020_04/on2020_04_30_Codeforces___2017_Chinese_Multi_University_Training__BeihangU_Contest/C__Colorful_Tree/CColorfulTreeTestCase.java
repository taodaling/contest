package on2020_04.on2020_04_30_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.C__Colorful_Tree;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class CColorfulTreeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
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
        StringBuilder builder = new StringBuilder(3000000);
        for(int i = 0; i < 1; i++){
            int n = 200000;
            printLine(builder, n);
            for(int j = 1; j <= n; j++){
                builder.append(random.nextInt(1, n)).append(' ');
            }
            builder.append('\n');
            for(int j = 2; j <= n; j++){
                printLine(builder, random.nextInt(1, j - 1), j);
            }
        }
        return new Test(builder.toString(), null);
    }


}
