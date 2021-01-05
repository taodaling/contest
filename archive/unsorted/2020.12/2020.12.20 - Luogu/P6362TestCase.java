package contest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P6362TestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
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

    public Test create(int testNum) {
        File dir = new File("C:\\Users\\dalt\\Downloads");
        return new Test(FileUtils.readFile(dir, "P6362_11.in"));
//        int n = (int) 1e5;
//        StringBuilder in = new StringBuilder();
//        printLine(in, n);
//        int range = (int) 1e5;
//        for (int i = 0; i < n; i++) {
//            printLine(in, random.nextInt(-range, range), random.nextInt(-range, range));
//        }
//        return new Test(in.toString(), null);
    }
}
