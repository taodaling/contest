package on2020_12.on2020_12_11_Codeforces___Codeforces_Round__334__Div__1_.E__Pastoral_Oddities0;



import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.io.FileUtils;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EPastoralOdditiesTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int n = (int)1e5;
        int m = (int)3e5;
        int[][] edges = new int[m][3];
        for(int i = 0; i < m; i++){
            edges[i][0] = random.nextInt(1, n);
            edges[i][1] = random.nextInt(1, n);
            edges[i][2] = random.nextInt(1, (int)1e9);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int[] e : edges){
            printLine(in, e);
        }
        return new Test(in.toString(), null);
    }
}
