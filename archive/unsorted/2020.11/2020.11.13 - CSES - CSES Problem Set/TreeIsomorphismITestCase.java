package contest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class TreeIsomorphismITestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        if(testNum == 0){
            File dir = new File("/home/dalt/下载");
            return new Test(FileUtils.readFile(new File(dir, "test_input.txt")),
                    FileUtils.readFile(new File(dir, "test_output.txt")));
        }

        int n = random.nextInt(1, 100);
        int[][] edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            int p = random.nextInt(0, i);
            edges[i][0] = p;
            edges[i][1] = i + 1;
        }
        int[] perm = IntStream.range(0, n).toArray();
        Randomized.shuffle(perm, 1, n);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        for (int[] e : edges) {
            printLine(in, e[0] + 1, e[1] + 1);
        }
        for (int[] e : edges) {
            printLine(in, perm[e[0]] + 1, perm[e[1]] + 1);
        }
        return new Test(in.toString(), "YES");
    }
}
