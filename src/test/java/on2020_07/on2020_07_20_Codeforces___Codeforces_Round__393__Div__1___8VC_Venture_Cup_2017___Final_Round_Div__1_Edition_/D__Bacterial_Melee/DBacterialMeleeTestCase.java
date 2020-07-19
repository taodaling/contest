package on2020_07.on2020_07_20_Codeforces___Codeforces_Round__393__Div__1___8VC_Venture_Cup_2017___Final_Round_Div__1_Edition_.D__Bacterial_Melee;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DBacterialMeleeTestCase {
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
        StringBuilder in = new StringBuilder(10000);
        in.append(5000).append('\n');
        for(int i = 0; i < 5000; i++){
            in.append((char)(i % ('z' - 'a' + 1) + 'a'));
        }
        return new Test(in.toString(), null);
    }
}
