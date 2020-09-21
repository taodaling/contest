package on2020_09.on2020_09_18_Codeforces___Divide_by_Zero_2017_and_Codeforces_Round__399__Div__1___Div__2__combined_.E__Game_of_Stones;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EGameOfStonesTestCase {
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
        StringBuilder in = new StringBuilder();
        printLine(in, 60);
        for(int i = 1; i <= 60; i++){
            in.append(i).append(' ');
        }
        return new Test(in.toString(), null);
    }
}
