package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class BGameWithModuloTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 0; i++){
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
    public Test create(int testNum){
        String in = "1\n" + random.nextInt(1, (int)1e9);
        return new Test(in, "");
    }


}
