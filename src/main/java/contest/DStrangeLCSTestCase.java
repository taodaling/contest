package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class DStrangeLCSTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int...vals){
        for(int val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long...vals){
        for(long val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
            for (T val : vals) {
                builder.append(val).append(' ');
            }
            builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        int charset = 26 * 2;
        StringBuilder in = new StringBuilder();
        printLine(in, 5);

        StringBuilder sb = new StringBuilder();
        for(char i = 'a'; i <= 'z'; i++){
            sb.append(i).append(i);
        }
        for(char i = 'A'; i <= 'Z'; i++){
            sb.append(i).append(i);
        }
        char[] data = sb.toString().toCharArray();
        for(int t = 0; t < 5; t++){
            int n = 10;
            printLine(in, n);
            for(int i = 0; i < n; i++) {
                Randomized.shuffle(data);
                printLineObj(in, new String(data));
            }
        }
        return new Test(in.toString(), null);
    }
}
