package contest;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import net.egork.chelper.util.FileUtilities;
import template.rand.RandomWrapper;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 1; i <= 10; i++){
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

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        File in = new File("D:\\temporary\\3028\\" + testNum + ".in");
        File out = new File("D:\\temporary\\3028\\" + testNum + ".out");
        String inContent = FileUtils.readFile(in);
        String outContent = FileUtils.readFile(out);
        return new Test(inContent, outContent);
    }
}
